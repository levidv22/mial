package com.ln.mial.ecommerce.infraestructure.controller;

import com.ln.mial.ecommerce.app.service.PedidosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequestMapping("/admin/statistics")
public class AdminGrafico {

    private final PedidosService pedidosService;

    public AdminGrafico(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    @GetMapping("/yearly-monthly-orders")
    @ResponseBody
    public Map<Integer, Map<Integer, Long>> getYearlyMonthlyOrders() {
        return pedidosService.getYearlyMonthlyOrderCounts();
    }

    @GetMapping
    public String showStatisticsPage() {
        return "admin/grafico";
    }
}
