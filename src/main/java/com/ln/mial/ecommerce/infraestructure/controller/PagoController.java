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

    // Mostrar la vista de pago
    @GetMapping
    public String showPaymentPage(HttpSession session, Model model) {
        PedidosEntity order = (PedidosEntity) session.getAttribute("currentOrder");

        if (order == null) {
            return "redirect:/user/carrito"; // Redirigir al carrito si no hay pedido
        }

        // Obtener los productos del pedido
        List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order);
        BigDecimal totalAmount = orderDetails.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Añadir datos del usuario y productos al modelo
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("cart", orderDetails);
        model.addAttribute("totalAmount", totalAmount);

        return "pago";  // Redirigir a la vista de pago
    }

    // Procesar el pago
    @PostMapping
    public ModelAndView confirmarPago(@RequestParam("file") MultipartFile multipartfile, //parametros
            @RequestParam("shippingAddress") String shippingAddress,
            HttpSession session) throws IOException, MessagingException {
        PedidosEntity order = (PedidosEntity) session.getAttribute("currentOrder");

        if (order == null) {
            return new ModelAndView("redirect:/user/carrito"); // Si no hay pedido en proceso, redirigir
        }

        // Guardar la dirección de envío en el pedido
        order.setShippingAddress(shippingAddress);

        // Asegurarse de que el total esté actualizado
        BigDecimal totalAmount = order.getTotalAmount();
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            totalAmount = pedidosService.calculateTotal(order); // Calcular si no está guardado
            order.setTotalAmount(totalAmount);
            pedidosService.saveOrder(order);
        }

        // Validar tipo de archivo
        if (multipartfile.isEmpty() || !isValidImage(multipartfile)) {
            session.setAttribute("Error", "Solo se permiten imágenes en formatos JPG, PNG, GIF o WEBP.");
            return new ModelAndView("redirect:/user/checkout");
        }

        String imagePago = uploadFile.upload(multipartfile); // Guardar la imagen y obtener el nombre del archivo

        // guarda los datos en la bd (pagos)
        PagosEntity pago = new PagosEntity();
        pago.setAmount(order.getTotalAmount());
        pago.setPaymentDate(LocalDateTime.now());
        pago.setOrder(order);
        pago.setImagePago(imagePago);
        pagosService.savePayment(pago);

        // Cambiar el estado del pedido a PAGADO
        order.setStatusPedido(StatusPedido.PAGADO);
        pedidosService.saveOrder(order); // Guardar los cambios en el pedido

        // Actualizar el stock en el almacén
        for (DetallePedidosEntity orderDetail : detallePedidosService.getOrderDetailsByOrder(order)) {
            ProductosEntity product = orderDetail.getProduct();
            AlmacenEntity stock = almacenService.getStockByProductEntity(product).get(0);

            // Actualizar las salidas y balance bd (almacen)
            stock.setSalidas(stock.getSalidas() + orderDetail.getQuantity());
            stock.setBalance(stock.getEntradas() - stock.getSalidas());
            almacenService.saveStock(stock);
        }

        // Enviar correo electrónico al cliente
        UsuariosEntity user = (UsuariosEntity) session.getAttribute("user");
        if (user != null) {
            String email = user.getEmail();
            String subject = "Confirmación de Pago de Lencería MIAL";

            // Formatear la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy -- hh:mm a");
            String formattedDate = LocalDateTime.now().format(formatter);

            // Construir el cuerpo del correo con HTML
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

        // Remover el pedido del carrito
        session.removeAttribute("currentOrder");

        return new ModelAndView("redirect:/user/historial"); // Redirigir al historial de pedidos
    }

    // Método para validar si el archivo es una imagen
    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null
                && (contentType.equals("image/jpeg") || contentType.equals("image/png")
                || contentType.equals("image/gif") || contentType.equals("image/webp"));
    }

}
