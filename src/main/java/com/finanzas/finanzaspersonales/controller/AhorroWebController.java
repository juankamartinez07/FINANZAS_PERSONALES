package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.dto.AhorroResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenAhorrosDTO;
import com.finanzas.finanzaspersonales.entity.Ahorro;
import com.finanzas.finanzaspersonales.service.AhorroService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AhorroWebController {

    private final AhorroService ahorroService;

    public AhorroWebController(AhorroService ahorroService) {
        this.ahorroService = ahorroService;
    }

    @GetMapping("/ahorros")
    public String listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String buscar,
            Model model) {
        List<AhorroResumenDTO> ahorros = ahorroService.listarResumen(estado, buscar);
        ResumenAhorrosDTO resumen = ahorroService.obtenerResumenFiltrado(ahorros);
        model.addAttribute("ahorros", ahorros);
        model.addAttribute("resumen", resumen);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("buscar", buscar);
        model.addAttribute("hayFiltros", hayFiltros(estado, buscar));
        model.addAttribute("activePage", "ahorros");
        return "ahorros/index";
    }

    @GetMapping("/ahorros/nuevo")
    public String nuevo(Model model) {
        Ahorro ahorro = new Ahorro();
        ahorro.setActivo(true);
        ahorro.setAhorroActual(BigDecimal.ZERO);
        prepararFormulario(model, ahorro, "Nueva meta", "/ahorros/guardar", false);
        return "ahorros/formulario";
    }

    @PostMapping("/ahorros/guardar")
    public String guardar(
            @ModelAttribute Ahorro ahorro,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Ahorro ahorroGuardado = ahorroService.guardar(ahorro);
            redirectAttributes.addFlashAttribute("mensajeExito", "Meta de ahorro creada correctamente.");
            if (ahorroGuardado.getAhorroActual() != null
                    && ahorroGuardado.getMeta() != null
                    && ahorroGuardado.getAhorroActual().compareTo(ahorroGuardado.getMeta()) == 0) {
                redirectAttributes.addFlashAttribute("mensajeInfo", "Has completado tu meta de ahorro.");
            }
            return "redirect:/ahorros";
        } catch (RuntimeException exception) {
            prepararFormulario(model, ahorro, "Nueva meta", "/ahorros/guardar", false);
            model.addAttribute("mensajeError", exception.getMessage());
            return "ahorros/formulario";
        }
    }

    @GetMapping("/ahorros/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Ahorro ahorro = ahorroService.buscarPorId(id);
            prepararFormulario(model, ahorro, "Editar meta", "/ahorros/actualizar/" + id, true);
            return "ahorros/formulario";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/ahorros";
        }
    }

    @PostMapping("/ahorros/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @ModelAttribute Ahorro ahorro,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Ahorro ahorroActualizado = ahorroService.actualizar(id, ahorro);
            redirectAttributes.addFlashAttribute("mensajeExito", "Meta de ahorro actualizada correctamente.");
            if (ahorroActualizado.getAhorroActual() != null
                    && ahorroActualizado.getMeta() != null
                    && ahorroActualizado.getAhorroActual().compareTo(ahorroActualizado.getMeta()) == 0) {
                redirectAttributes.addFlashAttribute("mensajeInfo", "Has completado tu meta de ahorro.");
            }
            return "redirect:/ahorros";
        } catch (RuntimeException exception) {
            prepararFormulario(model, ahorro, "Editar meta", "/ahorros/actualizar/" + id, true);
            model.addAttribute("mensajeError", exception.getMessage());
            return "ahorros/formulario";
        }
    }

    @GetMapping("/ahorros/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            AhorroResumenDTO ahorro = ahorroService.obtenerResumenPorId(id);
            model.addAttribute("ahorro", ahorro);
            model.addAttribute("activePage", "ahorros");
            return "ahorros/detalle";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/ahorros";
        }
    }

    @PostMapping("/ahorros/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Ahorro ahorro = ahorroService.cambiarEstado(id);
            String mensaje = Boolean.TRUE.equals(ahorro.getActivo())
                    ? "Meta de ahorro activada correctamente."
                    : "Meta de ahorro desactivada correctamente.";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
        }
        return "redirect:/ahorros";
    }

    private void prepararFormulario(Model model, Ahorro ahorro, String titulo, String action, boolean mostrarActivo) {
        model.addAttribute("ahorro", ahorro);
        model.addAttribute("tituloFormulario", titulo);
        model.addAttribute("formAction", action);
        model.addAttribute("mostrarActivo", mostrarActivo);
        model.addAttribute("activePage", "ahorros");
    }

    private boolean hayFiltros(String estado, String buscar) {
        return (estado != null && !estado.trim().isEmpty() && !"TODAS".equalsIgnoreCase(estado))
                || (buscar != null && !buscar.trim().isEmpty());
    }
}
