package com.ln.mial.ecommerce.app.service;

import jakarta.servlet.http.HttpSession;

public class LogoutService {

    public LogoutService() {

    }

    public void logout(HttpSession httpSession) {
        httpSession.removeAttribute("iduser");
    }
}
