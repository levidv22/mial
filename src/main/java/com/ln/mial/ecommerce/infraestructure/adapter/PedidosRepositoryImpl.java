package com.ln.mial.ecommerce.infraestructure.adapter;

import com.ln.mial.ecommerce.infraestructure.entity.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.ln.mial.ecommerce.app.repository.PedidosRepository;

@Repository
public class PedidosRepositoryImpl implements PedidosRepository {
    private final PedidosCrudRepository orderCrudRepository;

    public PedidosRepositoryImpl(PedidosCrudRepository orderCrudRepository) {
        this.orderCrudRepository = orderCrudRepository;
    }

    @Override
    public PedidosEntity getOrderById(Integer id) {
        return orderCrudRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<PedidosEntity> getOrdersByUserAndStatus(UsuariosEntity userEntity, StatusPedido status) {
        return orderCrudRepository.findByUserAndStatusPedido(userEntity, status);
    }

    @Override
    public PedidosEntity saveOrder(PedidosEntity ordersEntity) {
        return orderCrudRepository.save(ordersEntity);
    }

    
    @Override
    public List<PedidosEntity> getOrdersByStatus(StatusPedido status) {
        return orderCrudRepository.findByStatusPedido(status);
    }
    
    @Override
public List<Object[]> getYearlyMonthlyOrderCounts() {
    return orderCrudRepository.findYearlyMonthlyOrderCounts();
}

}
