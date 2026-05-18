package com.ln.mial.ecommerce.infraestructure.controller;

import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminProController {

    private final ProductosService productService;
    private final CategoriasService categoriasService;
    private final AlmacenService almacenService;

    public AdminProController(ProductosService productService, CategoriasService categoriasService, AlmacenService almacenService) {
        this.productService = productService;
        this.categoriasService = categoriasService;
        this.almacenService = almacenService;
    }

    @GetMapping
    public String showProducts(Model model) {
        return showProductsByCategoryInternal(null, model);
    }

    @GetMapping("/category/{id}")
    public String showProductsByCategory(@PathVariable Integer id, Model model) {
        return showProductsByCategoryInternal(id, model);
    }

    private String showProductsByCategoryInternal(Integer categoryId, Model model) {
        List<ProductosEntity> products;
        if (categoryId == null) {
            products = productService.getProducts();
        } else {
            products = (List<ProductosEntity>) productService.getProductsByCategory(categoryId);
        }

        for (ProductosEntity product : products) {
            List<AlmacenEntity> stockList = almacenService.getStockByProductEntity(product);
            product.setBalance(stockList.isEmpty() ? 0 : stockList.get(0).getBalance());
        }

        Collections.reverse(products);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoriasService.getCategories());
        return "admin/productos";
    }
}
