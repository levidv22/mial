package com.ln.mial.ecommerce.infraestructure.adapter;

import com.ln.mial.ecommerce.infraestructure.entity.EnviosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnviosCrudRepository extends CrudRepository<EnviosEntity, Integer> {
    List<EnviosEntity> findByOrder(PedidosEntity orderEntity);
}
