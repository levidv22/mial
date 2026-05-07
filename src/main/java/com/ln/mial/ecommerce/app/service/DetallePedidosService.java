package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.DetallePedidosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;

import java.util.List;
import com.ln.mial.ecommerce.app.repository.DetallePedidosRepository;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;

public class DetallePedidosService {

    private final DetallePedidosRepository orderDetailsRepository;

    public DetallePedidosService(DetallePedidosRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public List<DetallePedidosEntity> getOrderDetails() {
        return orderDetailsRepository.getOrderDetails();
    }

    public DetallePedidosEntity getOrderDetailById(Integer id) {
        return orderDetailsRepository.getOrderDetailById(id);
    }

    public List<DetallePedidosEntity> getOrderDetailsByOrder(PedidosEntity orderEntity) {
        return orderDetailsRepository.getOrderDetailsByOrder(orderEntity);
    }

    public DetallePedidosEntity saveOrderDetail(DetallePedidosEntity orderDetailsEntity) {
        return orderDetailsRepository.saveOrderDetail(orderDetailsEntity);
    }
    
    public DetallePedidosEntity findByOrderAndProduct(PedidosEntity order, ProductosEntity product) {
    return orderDetailsRepository.findByOrderAndProduct(order, product);
}


    public boolean deleteOrderDetailById(Integer id) {
        return orderDetailsRepository.deleteOrderDetailById(id);
    }
}
