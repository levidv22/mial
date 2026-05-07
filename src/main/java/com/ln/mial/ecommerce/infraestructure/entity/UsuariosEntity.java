package com.ln.mial.ecommerce.infraestructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class UsuariosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String firstName;
    private String email;
    private String cellphone;
    private String password;
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private TypeUser typeUser;
}
