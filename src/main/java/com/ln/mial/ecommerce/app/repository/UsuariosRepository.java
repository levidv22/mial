package com.ln.mial.ecommerce.app.repository;

import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;

public interface UsuariosRepository {
    
    //crear un nuevo usuario
    Iterable<UsuariosEntity> getUsers();
    public UsuariosEntity createUser(UsuariosEntity userEntity);
    public UsuariosEntity findByEmail(String email);
    public UsuariosEntity findById(Integer id);
    
    
}
