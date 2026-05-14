package com.ln.mial.ecommerce.app.service;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import org.springframework.web.multipart.MultipartFile;
import com.ln.mial.ecommerce.app.repository.ProductosRepository;
import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;
import java.util.List;

public class ProductosService {

    private final ProductosRepository productRepository;
    private final UploadFile uploadFile;

    public ProductosService(ProductosRepository productRepository, UploadFile uploadFile) {
        this.productRepository = productRepository;
        this.uploadFile = uploadFile;
    }

    public List<ProductosEntity> getProducts() {
        return productRepository.getProducts();
    }

    public Iterable<ProductosEntity> getProductsByUser(UsuariosEntity userEntity) {
        return productRepository.getProductsByUser(userEntity);
    }

    public ProductosEntity getProductById(Integer id) {
        return productRepository.getProductById(id);
    }

    public ProductosEntity saveProduct(ProductosEntity productEntity, MultipartFile multipartfile, HttpSession session) throws IOException {
        if (productEntity.getId() == null || productEntity.getId() == 0) {
            // Nuevo producto
            UsuariosEntity user = new UsuariosEntity();
            user.setId(Integer.parseInt(session.getAttribute("iduser").toString()));
            productEntity.setDateCreated(LocalDateTime.now());
            productEntity.setDateUpdated(LocalDateTime.now());
            productEntity.setUserEntity(user);
            productEntity.setImage(uploadFile.upload(multipartfile)); // Guarda la imagen
            return productRepository.saveProduct(productEntity);
        } else {
            ProductosEntity productDB = productRepository.getProductById(productEntity.getId());

            if (productDB == null) {
                throw new RuntimeException("Producto no encontrado");
            }

            productEntity.setId(productDB.getId());

            if (multipartfile.isEmpty()) {
                productEntity.setImage(productDB.getImage());
            } else {
                if (productDB.getImage() != null && !productDB.getImage().equals("default.png")) {
                    uploadFile.delete(productDB.getImage());
                }
                productEntity.setImage(uploadFile.upload(multipartfile));
            }

            productEntity.setUserEntity(productDB.getUserEntity());
            productEntity.setDateCreated(productDB.getDateCreated());
            productEntity.setDateUpdated(LocalDateTime.now());

            return productRepository.saveProduct(productEntity);
        }
    }

    public Iterable<ProductosEntity> getProductsByCategory(Integer categoryId) {
        CategoriasEntity category = new CategoriasEntity();
        category.setId(categoryId);
        return productRepository.findByCategory(category);
    }

    public boolean deleteProductById(Integer id) {
        return productRepository.deleteProductById(id);
    }
}