package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.EnviosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;

import java.util.List;

public interface EnviosRepository {
    //crear metodos
    List<EnviosEntity> getShipping();
    EnviosEntity getShippingById(Integer id);
    List<EnviosEntity> getShippingByOrder(PedidosEntity orderEntity);//mostrar envio por pedido
    EnviosEntity saveShipping(EnviosEntity shippingEntity);
    boolean deleteShippingById(Integer id);
}
