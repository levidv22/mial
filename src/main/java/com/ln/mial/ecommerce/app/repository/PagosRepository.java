package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.PagosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;

import java.util.List;

public interface PagosRepository {
    List<PagosEntity> getPayments();
    PagosEntity getPaymentById(Integer id);
    List<PagosEntity> getPaymentsByOrder(PedidosEntity orderEntity);
    PagosEntity savePayment(PagosEntity paymentsEntity);
}
