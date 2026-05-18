package com.ln.mial.ecommerce.infraestructure.controller;

import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/compras")
public class AdminComController {

    private final DetallePedidosService detallePedidosService;
    private final PedidosService pedidosService;
    private final PagosService pagosService;
    private final EnviosService enviosService;
    private final Logger log = LoggerFactory.getLogger(AdminComController.class);

    public AdminComController(DetallePedidosService detallePedidosService, PedidosService pedidosService, PagosService pagosService, EnviosService enviosService) {
        this.detallePedidosService = detallePedidosService;
        this.pedidosService = pedidosService;
        this.pagosService = pagosService;
        this.enviosService = enviosService;
    }

    @GetMapping
    public String showCompras(Model model) {
        List<PedidosEntity> allPaidOrders = pedidosService.getOrdersByStatus(StatusPedido.PAGADO);

        List<Map<String, Object>> pedidosAgrupados = new ArrayList<>();

        for (PedidosEntity order : allPaidOrders) {
            List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order);
            PagosEntity payment = pagosService.getPaymentsByOrder(order).stream().findFirst().orElse(null);

            Map<String, Object> pedidoAgrupado = new HashMap<>();
            pedidoAgrupado.put("name", order.getUser().getFirstName());
            pedidoAgrupado.put("numero", order.getUser().getCellphone());
            pedidoAgrupado.put("detallesPedido", orderDetails);
            pedidoAgrupado.put("shippingAddress", order.getShippingAddress());
            pedidoAgrupado.put("totalAmount", order.getTotalAmount());
            pedidoAgrupado.put("imagenPago", payment != null ? payment.getImagePago() : null);

            pedidosAgrupados.add(pedidoAgrupado);
        }

        Collections.reverse(pedidosAgrupados);

        model.addAttribute("pedidosAgrupados", pedidosAgrupados);

        return "admin/compras";
    }

    @GetMapping("/envio/{pedidoId}")
    public String showShippingForm(@PathVariable Integer pedidoId, Model model) {
        PedidosEntity pedido = pedidosService.getOrderById(pedidoId);
    if (pedido == null) {
        return "redirect:/historial-compras";
    }

        EnviosEntity envio = enviosService.getShippingByOrder(pedido).stream().findFirst().orElse(null);

        if (envio != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy -- hh:mm a");
            model.addAttribute("shippingDateFormatted", envio.getShippingDate().format(formatter));
            model.addAttribute("estimatedDeliveryDateFormatted", envio.getEstimatedDeliveryDate().format(formatter));
        }

        model.addAttribute("pedido", pedido);
        model.addAttribute("envio", envio);
        return "admin/agregar-envio";
    }

    @PostMapping("/envio")
    public String saveShippingDetails(
            @RequestParam("pedidoId") Integer pedidoId,
            @RequestParam("shippingMethod") String shippingMethod,
            @RequestParam("shippingDate") String shippingDate,
            @RequestParam("estimatedDeliveryDate") String estimatedDeliveryDate,
            RedirectAttributes redirectAttributes) {

        try {
            PedidosEntity pedido = pedidosService.getOrderById(pedidoId);

            LocalDateTime parsedShippingDate = LocalDateTime.parse(shippingDate);
            LocalDateTime parsedEstimatedDeliveryDate = LocalDateTime.parse(estimatedDeliveryDate);

            EnviosEntity envio = new EnviosEntity();
            envio.setOrder(pedido);
            envio.setShippingMethod(shippingMethod);
            envio.setShippingDate(parsedShippingDate);
            envio.setEstimatedDeliveryDate(parsedEstimatedDeliveryDate);
            envio.setShippingStatus("PENDIENTE");

            enviosService.saveShipping(envio);

            redirectAttributes.addFlashAttribute("success", "Detalles del envío agregados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un error al guardar los detalles del envío.");
        }

        return "redirect:/admin/compras";
    }
}
