package com.ln.mial.ecommerce.infraestructure.adapter;

import com.ln.mial.ecommerce.infraestructure.entity.PagosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;

public interface PagosCrudRepository extends CrudRepository<PagosEntity, Integer> {
    List<PagosEntity> findByOrder(PedidosEntity orderEntity);
    List<PagosEntity> findAllByOrderUser(UsuariosEntity user);

}
