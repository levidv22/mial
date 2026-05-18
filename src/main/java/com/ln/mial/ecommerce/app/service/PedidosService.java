package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import java.util.List;
import com.ln.mial.ecommerce.app.repository.PedidosRepository;
import com.ln.mial.ecommerce.infraestructure.entity.DetallePedidosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.StatusPedido;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class PedidosService {

    private final PedidosRepository orderRepository;
    private final DetallePedidosService detallePedidosService;

    public PedidosService(PedidosRepository orderRepository, DetallePedidosService detallePedidosService) {
        this.orderRepository = orderRepository;
        this.detallePedidosService = detallePedidosService;
    }

    public PedidosEntity getOrderById(Integer id) {
        return orderRepository.getOrderById(id);
    }

    public PedidosEntity saveOrder(PedidosEntity ordersEntity) {
        return orderRepository.saveOrder(ordersEntity);
    }

    public List<PedidosEntity> getOrdersByStatus(StatusPedido status) {
        return orderRepository.getOrdersByStatus(status);
    }

    public List<PedidosEntity> getOrdersByUserAndStatus(UsuariosEntity userEntity, StatusPedido status) {
        return orderRepository.getOrdersByUserAndStatus(userEntity, status);
    }

    public Map<Integer, Map<Integer, Long>> getYearlyMonthlyOrderCounts() {
        List<Object[]> results = orderRepository.getYearlyMonthlyOrderCounts();
        return results.stream()
                .collect(Collectors.groupingBy(
                        r -> (Integer) r[0], // Año
                        Collectors.toMap(
                                r -> (Integer) r[1], // Mes
                                r -> (Long) r[2], // Cantidad de compras
                                (oldValue, newValue) -> oldValue // En caso de conflicto, mantener el primero
                        )
                ));
    }
    
    public BigDecimal calculateTotal(PedidosEntity order) {
        List<DetallePedidosEntity> orderDetails = detallePedidosService.getOrderDetailsByOrder(order);

        return orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
