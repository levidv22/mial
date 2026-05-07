package com.ln.mial.ecommerce.app.repository;

import java.util.List;
import com.ln.mial.ecommerce.infraestructure.entity.AlmacenEntity;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;

public interface AlmacenRepository {
        // especifica métodos que interctuan con las varibles del entity
    AlmacenEntity saveStock(AlmacenEntity stockEntity);
    List<AlmacenEntity> getStockByProductEntity(ProductosEntity productosEntity);  
    boolean deleteStockById(Integer id);
}
