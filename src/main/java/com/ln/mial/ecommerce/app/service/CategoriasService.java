package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;
import com.ln.mial.ecommerce.app.repository.CategoriasRepository;

public class CategoriasService {
    private final CategoriasRepository categoryRepository;
    

    public CategoriasService(CategoriasRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<CategoriasEntity> getCategories() {
        return categoryRepository.getCategories();
    }

    public CategoriasEntity getCategoryById(Integer id) {
        return categoryRepository.getCategoryById(id);
    }

    public CategoriasEntity saveCategory(CategoriasEntity categoriesEntity) {
        return categoryRepository.saveCategory(categoriesEntity);
    }
}

