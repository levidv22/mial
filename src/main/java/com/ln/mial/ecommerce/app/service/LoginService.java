package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.TypeUser;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;

public class LoginService {

    private final UsuariosService usuariosService;

    public LoginService(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }


    public Integer getUserId(String email) {
        try {
            return usuariosService.findByEmail(email).getId();
        } catch (Exception e) {
            return 0;
        }
    }

    public TypeUser getUserType(String email) {
        return usuariosService.findByEmail(email).getTypeUser();
    }

    public UsuariosEntity getuser(String email) {
        try {
            return usuariosService.findByEmail(email);
        } catch (Exception e) {
            return new UsuariosEntity();
        }
    }

    public UsuariosEntity getUser(Integer id) {
        try {
            return usuariosService.findById(id);
        } catch (Exception e) {
            return new UsuariosEntity();
        }
    }
}
