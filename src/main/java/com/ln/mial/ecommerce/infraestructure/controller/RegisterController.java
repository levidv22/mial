package com.ln.mial.ecommerce.infraestructure.controller;

import com.ln.mial.ecommerce.app.service.*;
import com.ln.mial.ecommerce.infraestructure.dto.UserDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final Logger log = LoggerFactory.getLogger(RegisterController.class);
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping
    public String showRegister(UserDto userDto) {
        return "cuenta/register";
    }

    @PostMapping
    public String registerUser(@Valid UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> log.info("Error {}", e.getDefaultMessage()));
            return "cuenta/register";
        }

        try {
            registerService.register(userDto.userDtoToUser());
            redirectAttributes.addFlashAttribute("success", "Usuario creado correctamente");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

}
