package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.repository.ConceptoRepository;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.service.ReporteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReporteController {

    private final ReporteService reporteService;
    private final CuentaRepository cuentaRepository;
    private final ConceptoRepository conceptoRepository;

    public ReporteController(
            ReporteService reporteService,
            CuentaRepository cuentaRepository,
            ConceptoRepository conceptoRepository) {
        this.reporteService = reporteService;
        this.cuentaRepository = cuentaRepository;
        this.conceptoRepository = conceptoRepository;
    }

    @GetMapping("/reportes")
    public String reportes(
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            @RequestParam(required = false) Integer cuenta,
            @RequestParam(required = false) Integer concepto,
            @RequestParam(required = false) TipoConcepto tipo,
            Model model) {
        try {
            model.addAttribute("reporte", reporteService.obtenerReporte(periodo, desde, hasta, cuenta, concepto, tipo));
        } catch (RuntimeException exception) {
            model.addAttribute("mensajeError", exception.getMessage());
            model.addAttribute("reporte", reporteService.obtenerReporte("mesActual", null, null, cuenta, concepto, tipo));
        }
        model.addAttribute("cuentas", cuentaRepository.findAll());
        model.addAttribute("conceptos", conceptoRepository.findAll());
        model.addAttribute("tipos", TipoConcepto.values());
        model.addAttribute("activePage", "reportes");
        return "reportes/index";
    }
}
