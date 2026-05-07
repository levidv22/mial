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
        // Obtener todos los pedidos con estado PAGADO
        List<PedidosEntity> allPaidOrders = pedidosService.getOrdersByStatus(StatusPedido.PAGADO);

        // Crear una lista para almacenar los datos necesarios
        List<Map<String, Object>> pedidosAgrupados = new ArrayList<>();

        for (PedidosEntity order : allPaidOrders) {
            // Obtener detalles del pedido
            List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order);
            // Obtener el pago asociado al pedido
            PagosEntity payment = pagosService.getPaymentsByOrder(order).stream().findFirst().orElse(null);

            // Crear un mapa para agrupar los detalles de cada pedido
            Map<String, Object> pedidoAgrupado = new HashMap<>();
            pedidoAgrupado.put("name", order.getUser().getFirstName()); // Nombre de usuario
            pedidoAgrupado.put("numero", order.getUser().getCellphone()); // Nombre de usuario
            pedidoAgrupado.put("detallesPedido", orderDetails); // Detalles del pedido
            pedidoAgrupado.put("shippingAddress", order.getShippingAddress()); // Dirección de envío
            pedidoAgrupado.put("totalAmount", order.getTotalAmount()); // Monto total del pedido
            pedidoAgrupado.put("imagenPago", payment != null ? payment.getImagePago() : null); // Imagen de pago si existe

            // Añadir el mapa a la lista de pedidos agrupados
            pedidosAgrupados.add(pedidoAgrupado);
        }

        // Invertir la lista para mostrar los más recientes primero
        Collections.reverse(pedidosAgrupados);

        // Añadir los detalles agrupados al modelo
        model.addAttribute("pedidosAgrupados", pedidosAgrupados);

        return "admin/compras"; // Vista donde mostrar las compras agrupadas para el admin
    }

    // Mostrar vista para agregar el envío
    @GetMapping("/envio/{pedidoId}")
    public String showShippingForm(@PathVariable Integer pedidoId, Model model) {//poniendo las variables que se utilizará
        PedidosEntity pedido = pedidosService.getOrderById(pedidoId);
    if (pedido == null) {
        return "redirect:/historial-compras"; // Redirigir si el pedido no existe
    }

        EnviosEntity envio = enviosService.getShippingByOrder(pedido).stream().findFirst().orElse(null); // envios no nulo

        if (envio != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy -- hh:mm a");
            model.addAttribute("shippingDateFormatted", envio.getShippingDate().format(formatter));
            model.addAttribute("estimatedDeliveryDateFormatted", envio.getEstimatedDeliveryDate().format(formatter));
        }

        model.addAttribute("pedido", pedido);
        model.addAttribute("envio", envio);
        return "admin/agregar-envio"; // Vista para agregar envío
    }

    // Manejar el envío del formulario para agregar detalles de envío
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

            // Crear y guardar el envío
            EnviosEntity envio = new EnviosEntity();
            envio.setOrder(pedido);
            envio.setShippingMethod(shippingMethod);
            envio.setShippingDate(parsedShippingDate);
            envio.setEstimatedDeliveryDate(parsedEstimatedDeliveryDate);
            envio.setShippingStatus("PENDIENTE"); // Estado inicial

            enviosService.saveShipping(envio);

            redirectAttributes.addFlashAttribute("success", "Detalles del envío agregados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un error al guardar los detalles del envío.");
        }

        return "redirect:/admin/compras";
    }
}
