package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.DetallePedidosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;

import java.util.List;

public interface DetallePedidosRepository {
    List<DetallePedidosEntity> getOrderDetails();
    DetallePedidosEntity getOrderDetailById(Integer id);
    List<DetallePedidosEntity> getOrderDetailsByOrder(PedidosEntity orderEntity);
    DetallePedidosEntity findByOrderAndProduct(PedidosEntity order, ProductosEntity product);
    DetallePedidosEntity saveOrderDetail(DetallePedidosEntity orderDetailsEntity);
    boolean deleteOrderDetailById(Integer id);
}

