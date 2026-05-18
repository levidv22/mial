package com.ln.mial.ecommerce.infraestructure.controller;

import jakarta.servlet.http.*;
import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import java.math.*;
import java.util.List;
import com.ln.mial.ecommerce.app.service.AlmacenService;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/user/carrito")
public class PedidoController {

    private final PedidosService pedidosService;
    private final DetallePedidosService detallePedidosService;
    private final AlmacenService almacenService;

    public PedidoController(PedidosService pedidosService, DetallePedidosService detallePedidosService, AlmacenService almacenService) {
        this.pedidosService = pedidosService;
        this.detallePedidosService = detallePedidosService;
        this.almacenService = almacenService;
    }

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        PedidosEntity order = (PedidosEntity) session.getAttribute("currentOrder");

        if (order == null || order.getStatusPedido() == StatusPedido.PAGADO) {
            model.addAttribute("cart", List.of());
            model.addAttribute("totalAmount", BigDecimal.ZERO);
        } else {
            List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order).stream()
                    .filter(detail -> order.getStatusPedido() != StatusPedido.PAGADO)
                    .toList();

            BigDecimal totalAmount = orderDetails.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("cart", orderDetails);
            model.addAttribute("totalAmount", totalAmount);
        }

        return "carrito";
    }

    @GetMapping("/cantidad")
    @ResponseBody
    public int getCartItemCount(HttpSession session) {
        PedidosEntity order = (PedidosEntity) session.getAttribute("currentOrder");
        if (order == null || order.getStatusPedido() == StatusPedido.PAGADO) {
            return 0;
        }

        List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order);
        return orderDetails.size();
    }

    @PostMapping("/delete/{id}")
    public String deleteFromCart(@PathVariable("id") Integer orderDetailId) {
        detallePedidosService.deleteOrderDetailById(orderDetailId);
        return "redirect:/user/carrito";
    }

    @PostMapping("/update/{id}")
    public String updateCartItem(@PathVariable("id") Integer orderDetailId,
            @RequestParam("newQuantity") Integer newQuantity,
            HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            DetallePedidosEntity orderDetail = detallePedidosService.getOrderDetailById(orderDetailId);
            if (orderDetail == null) {
                redirectAttributes.addFlashAttribute("error", "El producto no existe en el carrito.");
                return "redirect:/user/carrito";
            }

            ProductosEntity product = orderDetail.getProduct();
            AlmacenEntity stock = almacenService.getStockByProductEntity(product).get(0);

            if (newQuantity > stock.getBalance()) {
                redirectAttributes.addFlashAttribute("error", "No puedes agregar más productos que los disponibles en stock.");
                return "redirect:/user/carrito";
            }

            BigDecimal oldTotal = orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity()));
            orderDetail.setQuantity(newQuantity);
            detallePedidosService.saveOrderDetail(orderDetail);

            PedidosEntity order = orderDetail.getOrder();
            order.setTotalAmount(order.getTotalAmount().subtract(oldTotal)
                    .add(orderDetail.getPrice().multiply(BigDecimal.valueOf(newQuantity))));
            pedidosService.saveOrder(order);

            redirectAttributes.addFlashAttribute("success", "Cantidad actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un error al actualizar la cantidad.");
        }
        return "redirect:/user/carrito";
    }

}
