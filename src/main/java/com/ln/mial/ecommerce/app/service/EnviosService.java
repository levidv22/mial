package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.EnviosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.PedidosEntity;

import java.util.List;
import com.ln.mial.ecommerce.app.repository.EnviosRepository;
import java.time.LocalDateTime;

public class EnviosService {
    private final EnviosRepository shippingRepository;

    public EnviosService(EnviosRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    public List<EnviosEntity> getShippingByOrder(PedidosEntity orderEntity) {
        return shippingRepository.getShippingByOrder(orderEntity);
    }

    public EnviosEntity saveShipping(EnviosEntity shippingEntity) {
        updateShippingStatus(shippingEntity);
        return shippingRepository.saveShipping(shippingEntity);
    }

    private void updateShippingStatus(EnviosEntity shippingEntity) {
        LocalDateTime now = LocalDateTime.now();

        if (shippingEntity.getShippingDate() != null && now.isAfter(shippingEntity.getShippingDate()) &&
            (shippingEntity.getShippingStatus() == null || shippingEntity.getShippingStatus().equals("PENDIENTE"))) {
            shippingEntity.setShippingStatus("EN_CAMINO");
        }

        if (shippingEntity.getEstimatedDeliveryDate() != null && now.isAfter(shippingEntity.getEstimatedDeliveryDate()) &&
            (shippingEntity.getShippingStatus() != null && shippingEntity.getShippingStatus().equals("EN_CAMINO"))) {
            shippingEntity.setShippingStatus("ENTREGADO");
        }
    }

}
