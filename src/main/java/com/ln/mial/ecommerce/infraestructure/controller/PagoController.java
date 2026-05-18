package com.ln.mial.ecommerce.infraestructure.controller;

import jakarta.servlet.http.*;
import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.*;
import java.io.*;
import java.math.*;
import java.time.*;
import java.util.*;
import com.ln.mial.ecommerce.app.service.DetallePedidosService;
import com.ln.mial.ecommerce.app.service.UploadFile;
import com.ln.mial.ecommerce.infraestructure.entity.StatusPedido;
import jakarta.mail.MessagingException;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/user/checkout")
public class PagoController {

    private final PagosService pagosService;
    private final DetallePedidosService detallePedidosService;
    private final UploadFile uploadFile;
    private final PedidosService pedidosService;
    private final AlmacenService almacenService;
    private final EmailService emailService;

    public PagoController(PagosService pagosService, DetallePedidosService detallePedidosService, UploadFile uploadFile, PedidosService pedidosService, AlmacenService almacenService, EmailService emailService) {
        this.pagosService = pagosService;
        this.detallePedidosService = detallePedidosService;
        this.uploadFile = uploadFile;
        this.pedidosService = pedidosService;
        this.almacenService = almacenService;
        this.emailService = emailService;
    }

    @GetMapping
    public String showPaymentPage(HttpSession session, Model model) {
        PedidosEntity order = (PedidosEntity) session.getAttribute("currentOrder");

        if (order == null) {
            return "redirect:/user/carrito";
        }

        List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order);
        BigDecimal totalAmount = orderDetails.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("cart", orderDetails);
        model.addAttribute("totalAmount", totalAmount);

        return "pago";
    }

    @PostMapping
    public ModelAndView confirmarPago(@RequestParam("file") MultipartFile multipartfile,
            @RequestParam("shippingAddress") String shippingAddress,
            HttpSession session) throws IOException, MessagingException {
        PedidosEntity order = (PedidosEntity) session.getAttribute("currentOrder");

        if (order == null) {
            return new ModelAndView("redirect:/user/carrito");
        }

        order.setShippingAddress(shippingAddress);

        BigDecimal totalAmount = order.getTotalAmount();
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            totalAmount = pedidosService.calculateTotal(order);
            order.setTotalAmount(totalAmount);
            pedidosService.saveOrder(order);
        }

        if (multipartfile.isEmpty() || !isValidImage(multipartfile)) {
            session.setAttribute("Error", "Solo se permiten imágenes en formatos JPG, PNG, GIF o WEBP.");
            return new ModelAndView("redirect:/user/checkout");
        }

        String imagePago = uploadFile.upload(multipartfile);

        PagosEntity pago = new PagosEntity();
        pago.setAmount(order.getTotalAmount());
        pago.setPaymentDate(LocalDateTime.now());
        pago.setOrder(order);
        pago.setImagePago(imagePago);
        pagosService.savePayment(pago);

        order.setStatusPedido(StatusPedido.PAGADO);
        pedidosService.saveOrder(order);

        for (DetallePedidosEntity orderDetail : detallePedidosService.getOrderDetailsByOrder(order)) {
            ProductosEntity product = orderDetail.getProduct();
            AlmacenEntity stock = almacenService.getStockByProductEntity(product).get(0);

            stock.setSalidas(stock.getSalidas() + orderDetail.getQuantity());
            stock.setBalance(stock.getEntradas() - stock.getSalidas());
            almacenService.saveStock(stock);
        }

        UsuariosEntity user = (UsuariosEntity) session.getAttribute("user");
        if (user != null) {
            String email = user.getEmail();
            String subject = "Confirmación de Pago de Lencería MIAL";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy -- hh:mm a");
            String formattedDate = LocalDateTime.now().format(formatter);

            StringBuilder productDetails = new StringBuilder();
            for (DetallePedidosEntity detail : detallePedidosService.getOrderDetailsByOrder(order)) {
                productDetails.append("<tr>")
                        .append("<td>").append(detail.getProduct().getName()).append("</td>")
                        .append("<td>").append(detail.getQuantity()).append("</td>")
                        .append("<td>").append(detail.getPrice()).append("</td>")
                        .append("</tr>");
            }

            String body = String.format(
                    "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
                    + ".container { max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 10px; }"
                    + "h2 { color: #333333; }"
                    + "table { width: 100%%; border-collapse: collapse; margin-top: 20px; }"
                    + "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }"
                    + "th { background-color: #f8f8f8; }"
                    + ".footer { margin-top: 20px; font-size: 12px; text-align: center; color: #888888; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<h2>Gracias por tu compra, %s!</h2>"
                    + "<p>Estos son los detalles de tu pago:</p>"
                    + "<p><strong>Monto Total:</strong> %s</p>"
                    + "<p><strong>Fecha de Pago:</strong> %s</p>"
                    + "<p><strong>Dirección de Envío:</strong> %s</p>"
                    + "<h3>Productos Comprados:</h3>"
                    + "<table>"
                    + "<thead>"
                    + "<tr>"
                    + "<th>Producto</th>"
                    + "<th>Cantidad</th>"
                    + "<th>Precio</th>"
                    + "</tr>"
                    + "</thead>"
                    + "<tbody>"
                    + "%s"
                    + "</tbody>"
                    + "</table>"
                    + "<p class='footer'>Gracias por confiar en nosotros.</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>",
                    user.getFirstName(),
                    totalAmount.toString(),
                    formattedDate,
                    shippingAddress,
                    productDetails.toString()
            );

            try {
                emailService.sendEmail(email, subject, body);
            } catch (MessagingException e) {
                session.setAttribute("Error", "No se pudo enviar el correo de confirmación");
            }
        }

        session.removeAttribute("currentOrder");

        return new ModelAndView("redirect:/user/historial"); // Redirigir al historial de pedidos
    }

    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null
                && (contentType.equals("image/jpeg") || contentType.equals("image/png")
                || contentType.equals("image/gif") || contentType.equals("image/webp"));
    }

}
