package com.ln.mial.ecommerce.infraestructure.configuration;

import com.ln.mial.ecommerce.app.repository.*;
import com.ln.mial.ecommerce.app.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public UsuariosService userService(UsuariosRepository userRepository) {
        return new UsuariosService(userRepository);
    }

    @Bean
    public ProductosService productService(ProductosRepository productRepository, UploadFile uploadFile) {
        return new ProductosService(productRepository, uploadFile);
    }

    @Bean
    public UploadFile uploadFile() {
        return new UploadFile();
    }


    @Bean
    public CategoriasService categoryService(CategoriasRepository categoryRepository) {
        return new CategoriasService(categoryRepository);
    }

    @Bean
    public PedidosService orderService(PedidosRepository orderRepository, DetallePedidosService detallePedidosService) {
        return new PedidosService(orderRepository, detallePedidosService);
    }

    @Bean
    public DetallePedidosService orderDetailsService(DetallePedidosRepository orderDetailsRepository) {
        return new DetallePedidosService(orderDetailsRepository);
    }

    @Bean
    public PagosService paymentService(PagosRepository paymentRepository) {
        return new PagosService(paymentRepository);
    }

    @Bean
    public EnviosService shippingService(EnviosRepository shippingRepository) {
        return new EnviosService(shippingRepository);//hace que sea funcionable el service y el repository
    }

    @Bean
    public AlmacenService stockService(AlmacenRepository stockRepository) {
        return new AlmacenService(stockRepository);
    }

    @Bean
    public ValidateStock validateStock(AlmacenService stockService) {
        return new ValidateStock(stockService);
    }
    
    @Bean
    public LoginService loginService(UsuariosService usuariosService){
        return new LoginService(usuariosService);
    }
    
    @Bean
    public LogoutService logoutService(){
        return  new LogoutService();
    } 
    @Bean
    public RegisterService registrationService(UsuariosService usuariosService, PasswordEncoder passwordEncoder, EmailService emailService){
        return  new RegisterService(usuariosService, passwordEncoder, emailService);
    }
}
