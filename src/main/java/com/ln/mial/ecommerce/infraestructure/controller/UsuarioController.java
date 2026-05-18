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

    @GetMapping
    public String showIndex(Model model) {
        Iterable<ProductosEntity> products = productService.getProducts();
        Iterable<CategoriasEntity> categories = categoriasService.getCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable Integer id, Model model) {
        return showProductsByCategoryInternal(id, model);
    }

    private String showProductsByCategoryInternal(Integer categoryId, Model model) {
        Iterable<ProductosEntity> products = productService.getProductsByCategory(categoryId);
        Iterable<CategoriasEntity> categories = categoriasService.getCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/privacidad")
    public String showPrivacy() {
        return "politica/privacidad";
    }

}
