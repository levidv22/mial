package com.ln.mial.ecommerce.infraestructure.controller;

import jakarta.servlet.http.*;
import java.math.*;
import java.time.*;
import java.util.*;
import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping("/order-details")
public class DetallePedidosController {

    private final DetallePedidosService detallePedidosService;
    private final PedidosService pedidosService;
    private final ProductosService productService;
    private final AlmacenService almacenService;

    public DetallePedidosController(DetallePedidosService detallePedidosService, PedidosService pedidosService, ProductosService productService, AlmacenService almacenService) {
        this.detallePedidosService = detallePedidosService;
        this.pedidosService = pedidosService;
        this.productService = productService;
        this.almacenService = almacenService;
    }

    @GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable Integer id, Model model) {
        // Obtener el producto por su ID
        ProductosEntity product = productService.getProductById(id);

        // Obtener el stock de ese producto en el almacén
        List<AlmacenEntity> stockList = almacenService.getStockByProductEntity(product);

        // Verificar si hay stock
        if (!stockList.isEmpty()) {
            AlmacenEntity stock = stockList.get(0); // Suponiendo que solo hay uno
            model.addAttribute("balance", stock.getBalance());
        } else {
            model.addAttribute("balance", 0); // No hay stock disponible
        }

        // Agregar el producto al modelo
        model.addAttribute("product", product);
        return "product-detail"; // Nombre de la vista a mostrar
    }

    @PostMapping("/add")
    public String addOrderDetail(@RequestParam("productId") Integer productId,
                                 @RequestParam("quantity") Integer quantity,
                                 HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el usuario ha iniciado sesión
            UsuariosEntity user = (UsuariosEntity) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para realizar una compra.");
            return "redirect:/login";
        }
            // Obtener el producto
            ProductosEntity product = productService.getProductById(productId);

            // Obtener el stock de ese producto en el almacén
            List<AlmacenEntity> stockList = almacenService.getStockByProductEntity(product);
            if (stockList.isEmpty()) {
                // Si no existe el producto en el almacén, no puede agregarse al carrito
                redirectAttributes.addFlashAttribute("error", "Este producto no está disponible en stock.");
                return "redirect:/user/carrito";
            }

            AlmacenEntity stock = stockList.get(0);  // Asumiendo que hay solo un stock por producto

            // Verificar si el balance es menor  a 0
            if (stock.getBalance() <= 0) {
                redirectAttributes.addFlashAttribute("error", "No hay stocks disponibles para este producto.");
                return "redirect:/user/carrito";
            }

            // Verificar si la cantidad solicitada está disponible en el balance
            if (quantity > stock.getBalance()) {
                redirectAttributes.addFlashAttribute("error", "La cantidad solicitada excede el stock disponible.");
                return "redirect:/user/carrito";
            }

            // Crear o actualizar el pedido
            PedidosEntity order = (PedidosEntity) session.getAttribute("currentOrder");
            if (order == null) {
                order = new PedidosEntity();
                order.setOrderDate(LocalDateTime.now());
                order.setStatusPedido(StatusPedido.EN_PROCESO);
                order.setUser(user);
                order.setTotalAmount(BigDecimal.ZERO);
                order = pedidosService.saveOrder(order);
                session.setAttribute("currentOrder", order);
            }

            //si hay un producto existen en el carrito y quiere agrerr ese mismo producto se suma la cntidad producto en el pedido actual
            DetallePedidosEntity orderDetail = detallePedidosService.findByOrderAndProduct(order, product);

            if (orderDetail != null) {
                // Si el producto ya está en el pedido, actualizar la cantidad
                orderDetail.setQuantity(orderDetail.getQuantity() + quantity);
                detallePedidosService.saveOrderDetail(orderDetail);

                // Actualizar el total
                BigDecimal addedAmount = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                order.setTotalAmount(order.getTotalAmount().add(addedAmount));
            } else {
                // Si el producto no está en el pedido, crear un nuevo producto al carrito
                orderDetail = new DetallePedidosEntity();
                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setPrice(product.getPrice());
                orderDetail.setOrder(order);
                detallePedidosService.saveOrderDetail(orderDetail);

                // Actualizar el total    multiplics el precio por l cantidd
                BigDecimal newTotalAmount = order.getTotalAmount().add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                order.setTotalAmount(newTotalAmount);
            }


            redirectAttributes.addFlashAttribute("success", "Producto agregado correctamente.");
        } catch (Exception e) {
            // Mensaje de error
            redirectAttributes.addFlashAttribute("error", "Hubo un error al agregar el producto.");
        }
        return "redirect:/user/carrito";
    }

}
