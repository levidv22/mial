package com.ln.mial.ecommerce.infraestructure.adapter;

import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProductosCrudRepository extends CrudRepository<ProductosEntity, Integer>{
    List<ProductosEntity> findByUserEntity(UsuariosEntity userEntity);
    List<ProductosEntity> findByCategory(CategoriasEntity category);
}
