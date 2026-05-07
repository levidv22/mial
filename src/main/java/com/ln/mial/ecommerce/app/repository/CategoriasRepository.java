package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;

public interface CategoriasRepository {
    // especifica métodos que interctuan con las varibles del entity
    Iterable<CategoriasEntity> getCategories();
// muestra todas las categorias
    CategoriasEntity getCategoryById(Integer id);
// muestra las categorias por id
    CategoriasEntity saveCategory(CategoriasEntity categoriesEntity);
// sirve para agregar y para actualizar categorias
    boolean deleteCategoryById(Integer id);
// eliminar categorias
}
