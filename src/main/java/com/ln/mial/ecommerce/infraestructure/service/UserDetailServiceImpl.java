package com.ln.mial.ecommerce.infraestructure.service;

import com.ln.mial.ecommerce.app.service.LoginService;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final LoginService loginService;
    private final Integer USER_NOT_FOUND = 0;
    @Autowired
    private HttpSession httpSession;

    public UserDetailServiceImpl(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Integer idUser = loginService.getUserId(username);
        if (idUser != USER_NOT_FOUND) {
            UsuariosEntity user = loginService.getuser(username);
            httpSession.setAttribute("user", user);
            httpSession.setAttribute("iduser", user.getId());
            httpSession.setAttribute("firstName", user.getFirstName());
            httpSession.setAttribute("username", user.getUsername());
            httpSession.setAttribute("cellphone", user.getCellphone());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getTypeUser().name())
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado ");
        }
    }
}
