package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;

public interface CategoriasRepository {
    Iterable<CategoriasEntity> getCategories();
    CategoriasEntity getCategoryById(Integer id);
    CategoriasEntity saveCategory(CategoriasEntity categoriesEntity);
}
