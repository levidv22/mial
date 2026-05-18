package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.EnviosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;

import java.util.List;

public interface EnviosRepository {
    List<EnviosEntity> getShippingByOrder(PedidosEntity orderEntity);
    EnviosEntity saveShipping(EnviosEntity shippingEntity);
    boolean deleteShippingById(Integer id);
}
