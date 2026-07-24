package com.finanzas.finanzaspersonales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavegacionController {

    @GetMapping("/configuracion")
    public String configuracion(Model model) {
        return vistaEnConstruccion(model, "Configuracion", "configuracion");
    }

    private String vistaEnConstruccion(Model model, String titulo, String activePage) {
        model.addAttribute("tituloModulo", titulo);
        model.addAttribute("activePage", activePage);
        return activePage + "/index";
    }
}
