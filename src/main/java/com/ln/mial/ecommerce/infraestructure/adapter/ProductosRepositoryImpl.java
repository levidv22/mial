package com.ln.mial.ecommerce.infraestructure.adapter;

//import jakarta.transaction.Transactional;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import org.springframework.stereotype.Repository;
import com.ln.mial.ecommerce.app.repository.ProductosRepository;
import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ProductosRepositoryImpl implements ProductosRepository {
    private final ProductosCrudRepository productCrudRepository;

    public ProductosRepositoryImpl(ProductosCrudRepository productCrudRepository) {
        this.productCrudRepository = productCrudRepository;
    }

    @Override
    public List<ProductosEntity> getProducts() {
        return (List<ProductosEntity>) productCrudRepository.findAll();
    }

    @Override
    public Iterable<ProductosEntity> getProductsByUser(UsuariosEntity userEntity) {
        return productCrudRepository.findByUserEntity(userEntity);
    }

    @Override
    public Iterable<ProductosEntity> findByCategory(CategoriasEntity category) {
        return productCrudRepository.findByCategory(category);  // Add this method
    }

    @Override
    public ProductosEntity getProductById(Integer id) {
        return productCrudRepository.findById(id).get();
    }

    @Override
    public ProductosEntity saveProduct(ProductosEntity productEntity) {
        return productCrudRepository.save(productEntity);
    }

    @Override
    @Transactional
    public boolean deleteProductById(Integer id) {
        productCrudRepository.deleteById(id);
        return true;
    }
}
