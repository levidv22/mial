package com.ln.mial.ecommerce.infraestructure.controller;

import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.entity.*;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/create")
public class AdminFormController {

    private final ProductosService productService;
    private final CategoriasService categoriasService;
    private final Logger log = LoggerFactory.getLogger(AdminFormController.class);

    public AdminFormController(ProductosService productService, CategoriasService categoriasService) {
        this.productService = productService;
        this.categoriasService = categoriasService;
    }

    @GetMapping
    public String showCategory(Model model) {
        Iterable<CategoriasEntity> categories = categoriasService.getCategories();
        model.addAttribute("categories", categories);
        return "admin/formulario";
    }

    @PostMapping
    public String addProduct(ProductosEntity product,
                             @RequestParam("file") MultipartFile multipartfile,
                             @RequestParam("categoryId") Integer categoryId,
                             HttpSession session) throws IOException {

        CategoriasEntity category = categoriasService.getCategoryById(categoryId);
        product.setCategory(category);

        if (product.getId() == null) {

            if (multipartfile.isEmpty() || !isValidImage(multipartfile)) {
                session.setAttribute("Error", "Debes subir una imagen válida.");
                return "redirect:/admin/create";
            }

        }

        productService.saveProduct(product, multipartfile, session);
        log.info("ID recibido: {}", product.getId());
        log.info("Archivo: {}", multipartfile.getOriginalFilename());

        return "redirect:/admin/products";
    }

    // Método para validar si el archivo es una imagen
    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg")
                || contentType.equals("image/jpg")
                || contentType.equals("image/png")
                || contentType.equals("image/gif")
                || contentType.equals("image/webp"));
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        Iterable<CategoriasEntity> categories = categoriasService.getCategories();
        model.addAttribute("categories", categories);
        ProductosEntity product = productService.getProductById(id);
        log.info("Product obtenido: {}", product);
        model.addAttribute("product", product);
        return "admin/editar";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }

}