package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import com.ln.mial.ecommerce.app.repository.UsuariosRepository;

public class UsuariosService {
    //Intancia del user repository
    private final UsuariosRepository userRepository;

    
    //constructor
    public UsuariosService(UsuariosRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    //metodos
    
    public Iterable<UsuariosEntity> getUsers() {
        return userRepository.getUsers();
    }
        
    public UsuariosEntity createUser(UsuariosEntity userEntity) {
        return userRepository.createUser(userEntity);
    }

    public UsuariosEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UsuariosEntity findById(Integer id) {
        return userRepository.findById(id);
    }

}
