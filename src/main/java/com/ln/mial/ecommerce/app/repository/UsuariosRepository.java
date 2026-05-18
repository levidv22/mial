package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;

public interface UsuariosRepository {
    
    Iterable<UsuariosEntity> getUsers();
    UsuariosEntity createUser(UsuariosEntity userEntity);
    UsuariosEntity findByEmail(String email);
    UsuariosEntity findById(Integer id);
    
    
}
