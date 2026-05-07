package com.ln.mial.ecommerce.infraestructure.adapter;

import java.util.List;
import com.ln.mial.ecommerce.infraestructure.entity.AlmacenEntity;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;
import org.springframework.data.repository.CrudRepository;

public interface AlmacenCrudRepository extends CrudRepository<AlmacenEntity, Integer>{
    // se agrega algo siempre y cuando tenga una relacion de id con otra tabla
       List<AlmacenEntity> getStockByProductosEntity(ProductosEntity productosEntity); 
}
