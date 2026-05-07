package com.ln.mial.ecommerce.app.service;

import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UsuariosService usuariosService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RegisterService(UsuariosService usuariosService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuariosService = usuariosService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void register(UsuariosEntity user) {
        if (usuariosService.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UsuariosEntity savedUser = usuariosService.createUser(user);

        // Enviar correo de bienvenida
        String subject = "Bienvenido a LN MIAL Ecommerce";
        String body = String.format(
                "<h1>¡Hola, %s!</h1>"
                + "<p>Gracias por registrarte en LN MIAL Ecommerce. Estamos encantados de tenerte con nosotros.</p>"
                + "<p>Tu correo registrado es: %s</p>"
                + "<br><p>Atentamente,<br>Equipo LN MIAL</p>",
                savedUser.getFirstName(), savedUser.getEmail()
        );

        try {
            emailService.sendEmail(savedUser.getEmail(), subject, body);
        } catch (Exception e) {
            e.printStackTrace(); // Maneja errores de envío de correo
        }
    }
}
