package com.ln.mial.ecommerce.infraestructure.controller;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class HistorialComprasController {

    private final PagosService pagosService;
    private final PedidosService pedidosService;
    private final DetallePedidosService detallePedidosService;
    private final EnviosService enviosService;

    public HistorialComprasController(PagosService pagosService, PedidosService pedidosService, DetallePedidosService detallePedidosService, EnviosService enviosService) {
        this.pagosService = pagosService;
        this.pedidosService = pedidosService;
        this.detallePedidosService = detallePedidosService;
        this.enviosService = enviosService;
    }

    @GetMapping("/historial")
    public String showPurchasedProducts(HttpSession session, Model model) {
        UsuariosEntity user = (UsuariosEntity) session.getAttribute("user");

        List<PedidosEntity> paidOrders = pedidosService.getOrdersByUserAndStatus(user, StatusPedido.PAGADO);

        List<Map<String, Object>> pedidosAgrupados = new ArrayList<>();

        for (PedidosEntity order : paidOrders) {
            List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order);
            PagosEntity payment = pagosService.getPaymentsByOrder(order).stream().findFirst().orElse(null);

            Map<String, Object> pedidoAgrupado = new HashMap<>();
            pedidoAgrupado.put("username", order.getUser().getUsername());
            pedidoAgrupado.put("detallesPedido", orderDetails);
            pedidoAgrupado.put("totalAmount", order.getTotalAmount());
            pedidoAgrupado.put("shippingAddress", order.getShippingAddress());
            pedidoAgrupado.put("imagenPago", payment != null ? payment.getImagePago() : null);

            pedidosAgrupados.add(pedidoAgrupado);
        }

        Collections.reverse(pedidosAgrupados);

        model.addAttribute("pedidosAgrupados", pedidosAgrupados);

        return "historial-compras";
    }

    @GetMapping("/envio/{pedidoId}")
    public String showShippingForm(@PathVariable Integer pedidoId, Model model) {
        PedidosEntity pedido = pedidosService.getOrderById(pedidoId);
        if (pedido == null) {
            return "redirect:historial-compras";
        }

        EnviosEntity envio = enviosService.getShippingByOrder(pedido).stream().findFirst().orElse(null);

        if (envio != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy -- hh:mm a");
            model.addAttribute("shippingDateFormatted", envio.getShippingDate().format(formatter));
            model.addAttribute("estimatedDeliveryDateFormatted", envio.getEstimatedDeliveryDate().format(formatter));
        }

        model.addAttribute("pedido", pedido);
        model.addAttribute("envio", envio);
        return "ver-envio";
    }

}
