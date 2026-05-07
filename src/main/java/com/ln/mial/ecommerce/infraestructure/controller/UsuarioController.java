package com.ln.mial.ecommerce.infraestructure.controller;

import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/index/products")
public class UsuarioController {

    private final ProductosService productService;
    private final CategoriasService categoriasService;

    public UsuarioController(ProductosService productService, CategoriasService categoriasService) {
        this.productService = productService;
        this.categoriasService = categoriasService;
    }

// Ruta para mostrar productos (tanto para clientes autenticados como no autenticados)
    @GetMapping
    public String showIndex(Model model) {
        Iterable<ProductosEntity> products = productService.getProducts();//llama  los valores del entity y product.... get hce mostrar todos los productos
        // Obtener todas las categorías para los botones
        Iterable<CategoriasEntity> categories = categoriasService.getCategories();
        // Añadir productos y categorías al modelo
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index"; // Redirigir a la vista index.html
    }

// Ruta para mostrar productos por categoría
    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable Integer id, Model model) {
        // Mostrar productos según la categoría seleccionada
        return showProductsByCategoryInternal(id, model);
    }

//  mostrar productos por categoría
    private String showProductsByCategoryInternal(Integer categoryId, Model model) {
        // Obtener productos por categoría
        Iterable<ProductosEntity> products = productService.getProductsByCategory(categoryId);
        // Obtener todas las categorías para los botones
        Iterable<CategoriasEntity> categories = categoriasService.getCategories();
        // Añadir productos y categorías al modelo
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index"; // Redirigir a la vista index.html
    }

    @GetMapping("/privacidad")
    public String showPrivacy() {
        return "politica/privacidad";
    }

}
