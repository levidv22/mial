package com.ln.mial.ecommerce.infraestructure.adapter;

import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import com.ln.mial.ecommerce.infraestructure.entity.StatusPedido;
import org.springframework.data.jpa.repository.Query;

public interface PedidosCrudRepository extends CrudRepository<PedidosEntity, Integer> {
    List<PedidosEntity> findByUser(UsuariosEntity userEntity);
    List<PedidosEntity> findByUserAndStatusPedido(UsuariosEntity userEntity, StatusPedido status);
    List<PedidosEntity> findByStatusPedido(StatusPedido status);
    @Query("SELECT YEAR(p.orderDate) AS year, MONTH(p.orderDate) AS month, COUNT(p.id) AS count " +
       "FROM PedidosEntity p " +
       "WHERE p.statusPedido = 'PAGADO' " +
       "GROUP BY YEAR(p.orderDate), MONTH(p.orderDate) " +
       "ORDER BY YEAR(p.orderDate), MONTH(p.orderDate)")
List<Object[]> findYearlyMonthlyOrderCounts();



}
