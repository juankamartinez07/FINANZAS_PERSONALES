package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.service.AnalisisFinancieroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnalisisFinancieroController {

    private final AnalisisFinancieroService analisisFinancieroService;

    public AnalisisFinancieroController(AnalisisFinancieroService analisisFinancieroService) {
        this.analisisFinancieroService = analisisFinancieroService;
    }

    @GetMapping("/analisis-financiero")
    public String mostrarAnalisis(
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            Model model) {
        try {
            model.addAttribute("analisis", analisisFinancieroService.obtenerAnalisis(periodo, desde, hasta));
        } catch (IllegalArgumentException exception) {
            model.addAttribute("mensajeError", exception.getMessage());
            model.addAttribute("analisis", analisisFinancieroService.obtenerAnalisis("mesActual", null, null));
        }
        model.addAttribute("activePage", "analisis-financiero");
        return "analisis/index";
    }
}
