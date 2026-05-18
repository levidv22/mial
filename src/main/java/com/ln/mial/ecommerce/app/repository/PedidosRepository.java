package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;

import java.util.List;
import com.ln.mial.ecommerce.infraestructure.entity.StatusPedido;

public interface PedidosRepository {
    PedidosEntity getOrderById(Integer id);
    List<PedidosEntity> getOrdersByUserAndStatus(UsuariosEntity userEntity, StatusPedido status);
    PedidosEntity saveOrder(PedidosEntity ordersEntity);
    List<PedidosEntity> getOrdersByStatus(StatusPedido status);
    List<Object[]> getYearlyMonthlyOrderCounts();

}

