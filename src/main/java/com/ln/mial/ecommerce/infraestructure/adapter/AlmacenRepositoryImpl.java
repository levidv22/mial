package com.ln.mial.ecommerce.infraestructure.adapter;

import jakarta.transaction.Transactional;
import java.util.List;
import com.ln.mial.ecommerce.infraestructure.entity.AlmacenEntity;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;
import org.springframework.stereotype.Repository;
import com.ln.mial.ecommerce.app.repository.AlmacenRepository;

@Repository
public class AlmacenRepositoryImpl implements AlmacenRepository{
private final AlmacenCrudRepository stockCrudRepository;

    public AlmacenRepositoryImpl(AlmacenCrudRepository stockCrudRepository) {
        this.stockCrudRepository = stockCrudRepository;
    }

    @Override //sirve para sobreescribir los metodos ya existentes en la interfaz CategoriasRepository
    public AlmacenEntity saveStock(AlmacenEntity stockEntity) {
        return stockCrudRepository.save(stockEntity); //métodos listos para usar que simplifican el acceso a la base de datos
    }

    @Override
    public List<AlmacenEntity> getStockByProductEntity(ProductosEntity productosEntity) {
    return stockCrudRepository.getStockByProductosEntity(productosEntity);
    }
    
    @Override
    @Transactional
    public boolean deleteStockById(Integer id) {
        stockCrudRepository.deleteById(id);
        return true;
    }
    
}

