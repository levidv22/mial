package com.ln.mial.ecommerce.infraestructure.adapter;

import com.ln.mial.ecommerce.infraestructure.entity.CategoriasEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoriasCrudRepository extends CrudRepository<CategoriasEntity, Integer> {
// se agrega algo siempre y cuando tenga una relacion de id con otra tabla
}
