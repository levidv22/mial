package com.ln.mial.ecommerce.infraestructure.adapter;

import java.util.Optional;
import com.ln.mial.ecommerce.infraestructure.entity.UsuariosEntity;
import org.springframework.data.repository.CrudRepository;

public interface UsuariosCrudRepository extends CrudRepository<UsuariosEntity, Integer>{
   //METODO ADICIONAL PARA BUSCAR POR EMAIL
    public Optional<UsuariosEntity> findByEmail(String email);
    
}
