package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.TypeUser;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;

public class LoginService {

    private final UsuariosService usuariosService;

    public LoginService(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    //retorna true si encuentra el user
    public boolean existUser(String email) {
        try {
            UsuariosEntity user = usuariosService.findByEmail(email);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //obtenemos el id del usuario
    public Integer getUserId(String email) {
        try {
            return usuariosService.findByEmail(email).getId();
        } catch (Exception e) {
            return 0;
        }
    }
    //obtener tipo de usuario

    public TypeUser getUserType(String email) {
        return usuariosService.findByEmail(email).getTypeUser();
    }
    //obtenemos el user por email

    public UsuariosEntity getuser(String email) {
        try {
            return usuariosService.findByEmail(email);
        } catch (Exception e) {
            return new UsuariosEntity();
        }
    }

    //obtenemos el user por id
    public UsuariosEntity getUser(Integer id) {
        try {
            return usuariosService.findById(id);
        } catch (Exception e) {
            return new UsuariosEntity();
        }
    }
}
