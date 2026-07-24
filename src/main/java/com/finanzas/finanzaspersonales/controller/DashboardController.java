package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.service.DashboardService;
import com.finanzas.finanzaspersonales.service.AnalisisFinancieroService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final AnalisisFinancieroService analisisFinancieroService;

    public DashboardController(DashboardService dashboardService, AnalisisFinancieroService analisisFinancieroService) {
        this.dashboardService = dashboardService;
        this.analisisFinancieroService = analisisFinancieroService;
    }

    @GetMapping({"/", "/dashboard"})
    public String mostrarDashboard(@RequestParam(required = false) String mes, Model model) {
        YearMonth mesSeleccionado = resolverMesSeleccionado(mes);
        LocalDate inicioMes = mesSeleccionado.atDay(1);
        LocalDate finMes = mesSeleccionado.atEndOfMonth();

        model.addAttribute("dashboard", dashboardService.obtenerDashboardPorMes(
                mesSeleccionado.getYear(),
                mesSeleccionado.getMonthValue()));
        model.addAttribute("analisisResumen", analisisFinancieroService.obtenerAnalisis(
                "personalizado",
                inicioMes.toString(),
                finMes.toString()));
        model.addAttribute("fechaReferencia", inicioMes);
        model.addAttribute("mesSeleccionado", mesSeleccionado.toString());
        model.addAttribute("activePage", "dashboard");
        return "dashboard/index";
    }

    private YearMonth resolverMesSeleccionado(String mes) {
        if (mes == null || mes.trim().isEmpty()) {
            return YearMonth.from(LocalDate.now());
        }
        try {
            return YearMonth.parse(mes.trim());
        } catch (DateTimeParseException exception) {
            return YearMonth.from(LocalDate.now());
        }
    }
}
