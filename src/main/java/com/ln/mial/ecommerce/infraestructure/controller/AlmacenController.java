package com.ln.mial.ecommerce.infraestructure.controller;

import com.ln.mial.ecommerce.infraestructure.entity.AlmacenEntity;
import java.util.List;
import com.ln.mial.ecommerce.app.service.AlmacenService;
import com.ln.mial.ecommerce.app.service.ProductosService;
import com.ln.mial.ecommerce.app.service.ValidateStock;
import com.ln.mial.ecommerce.infraestructure.entity.ProductosEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/products/stock")
public class AlmacenController {

    private final AlmacenService stockService;
    private final ValidateStock validateStock;
    private final ProductosService productService;
    
    public AlmacenController(AlmacenService stockService, ValidateStock validateStock, ProductosService productService) {
        this.stockService = stockService;
        this.validateStock = validateStock;
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {
        ProductosEntity product = new ProductosEntity();
        product.setId(id);
        List<AlmacenEntity> stocks = stockService.getStockByProductEntity(product);
        model.addAttribute("stocks", stocks);
        model.addAttribute("idproduct", id);
        return "admin/stock";

    }

    @PostMapping
    public String save(AlmacenEntity stock, @RequestParam("idproduct") Integer idproduct) {
        ProductosEntity product = new ProductosEntity();
        product.setId(idproduct);

        AlmacenEntity existingStock = stockService.getStockByProduct(product);

        if (existingStock != null) {
            // Si el inventario ya existe, actualiza la entrada y el balance
            stockService.updateStock(existingStock, stock.getEntradas());
        } else {
            // Si no existe, crea un nuevo registro
            stock.setDescripcion("entradas");
            stock.setProductosEntity(product);
            stockService.saveStock(validateStock.calculateBalance(stock));
        }

        return "redirect:/admin/products";
    }
}
