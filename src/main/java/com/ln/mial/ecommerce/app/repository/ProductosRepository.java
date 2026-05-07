package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import java.util.List;

public interface ProductosRepository {
 
    List<ProductosEntity> getProducts();
    Iterable<ProductosEntity> getProductsByUser(UsuariosEntity userEntity);
    ProductosEntity getProductById(Integer id);
    ProductosEntity saveProduct(ProductosEntity productEntity);
    Iterable<ProductosEntity> findByCategory(CategoriasEntity category);
    boolean deleteProductById(Integer id);
    
}
