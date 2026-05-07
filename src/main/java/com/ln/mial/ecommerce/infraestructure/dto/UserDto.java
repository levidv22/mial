package com.ln.mial.ecommerce.infraestructure.dto;

import com.ln.mial.ecommerce.infraestructure.entity.TypeUser;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class UserDto {

    private String username;
    @NotBlank(message = "Nombre es requerido")
    private String firstName;
    @Email(message = "Debe ingresar un email valido")
    private String email;
    @NotBlank(message = "Celular es requerido")
    @Size(min = 9, message = "ingrese 9 digitos")
    private String cellphone;
    @NotBlank(message = "Clave es requerido")
    private String password;
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

    public UsuariosEntity userDtoToUser() {
        return new UsuariosEntity(
                null, 
                this.getEmail(), 
                this.getFirstName(),
                this.getEmail(), 
                this.getCellphone(), 
                this.getPassword(), 
                LocalDateTime.now(), 
                TypeUser.USER);
    }
}
