package com.ln.mial.ecommerce.infraestructure.configuration;

import com.ln.mial.ecommerce.infraestructure.service.UserDetailServiceImpl;
import com.ln.mial.ecommerce.infraestructure.service.loginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailServiceImpl userDetailService;
    @Autowired
    private loginHandler loginHandler;
    public SecurityConfig(UserDetailServiceImpl userDetailServiceImpl) {
        this.userDetailService = userDetailServiceImpl;
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER").anyRequest().permitAll())
                .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginHandler))
                .logout(log -> log
                .logoutSuccessUrl("/close"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
