package com.ln.mial.ecommerce.infraestructure.adapter;

import jakarta.transaction.Transactional;
import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;
import org.springframework.stereotype.Repository;
import com.ln.mial.ecommerce.app.repository.CategoriasRepository;

@Repository
public class CategoriasRepositoryImpl implements CategoriasRepository {
    private final CategoriasCrudRepository categoryCrudRepository;

    public CategoriasRepositoryImpl(CategoriasCrudRepository categoryCrudRepository) {
        this.categoryCrudRepository = categoryCrudRepository;
    }

    @Override //sirve para sobreescribir los metodos ya existentes en la interfaz CategoriasRepository
    public Iterable<CategoriasEntity> getCategories() {
        return categoryCrudRepository.findAll();//métodos listos para usar que simplifican el acceso a la base de datos
    }

    @Override
    public CategoriasEntity getCategoryById(Integer id) {
        return categoryCrudRepository.findById(id).orElse(null);
    }

    @Override
    public CategoriasEntity saveCategory(CategoriasEntity categoriesEntity) {
        return categoryCrudRepository.save(categoriesEntity);
    }

    @Override
    @Transactional
    public boolean deleteCategoryById(Integer id) {
        categoryCrudRepository.deleteById(id);
        return true;
    }
}
