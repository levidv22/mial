package com.ln.mial.ecommerce.infraestructure.adapter;

import jakarta.transaction.Transactional;
import com.ln.mial.ecommerce.infraestructure.entity.EnviosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.ln.mial.ecommerce.app.repository.EnviosRepository;

@Repository
public class EnviosRepositoryImpl implements EnviosRepository {
    private final EnviosCrudRepository shippingCrudRepository;

    public EnviosRepositoryImpl(EnviosCrudRepository shippingCrudRepository) {
        this.shippingCrudRepository = shippingCrudRepository;
    }

    @Override
    public List<EnviosEntity> getShippingByOrder(PedidosEntity orderEntity) {
        return shippingCrudRepository.findByOrder(orderEntity);
    }

    @Override
    public EnviosEntity saveShipping(EnviosEntity shippingEntity) {
        return shippingCrudRepository.save(shippingEntity);
    }

    @Override
    @Transactional
    public boolean deleteShippingById(Integer id) {
        shippingCrudRepository.deleteById(id);
        return true;
    }
}
