package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.dto.ConceptoResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenConceptosDTO;
import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.service.ConceptoService;
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
public class ConceptoWebController {

    private final ConceptoService conceptoService;

    public ConceptoWebController(ConceptoService conceptoService) {
        this.conceptoService = conceptoService;
    }

    @GetMapping("/conceptos")
    public String listar(
            @RequestParam(required = false) TipoConcepto tipo,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String buscar,
            Model model) {
        List<ConceptoResumenDTO> conceptos = conceptoService.listarResumen(tipo, activo, buscar);
        ResumenConceptosDTO resumen = conceptoService.obtenerResumenGeneral();
        model.addAttribute("conceptos", conceptos);
        model.addAttribute("resumen", resumen);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("activoSeleccionado", activo);
        model.addAttribute("buscar", buscar);
        model.addAttribute("tipos", TipoConcepto.values());
        model.addAttribute("hayFiltros", hayFiltros(tipo, activo, buscar));
        model.addAttribute("activePage", "conceptos");
        return "conceptos/index";
    }

    @GetMapping("/conceptos/nuevo")
    public String nuevo(Model model) {
        Concepto concepto = new Concepto();
        concepto.setActivo(true);
        prepararFormulario(model, concepto, "Nuevo concepto", "/conceptos/guardar", false, true);
        return "conceptos/formulario";
    }

    @PostMapping("/conceptos/guardar")
    public String guardar(
            @ModelAttribute Concepto concepto,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            conceptoService.guardar(concepto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Concepto creado correctamente.");
            return "redirect:/conceptos";
        } catch (RuntimeException exception) {
            prepararFormulario(model, concepto, "Nuevo concepto", "/conceptos/guardar", false, true);
            model.addAttribute("mensajeError", exception.getMessage());
            return "conceptos/formulario";
        }
    }

    @GetMapping("/conceptos/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Concepto concepto = conceptoService.buscarPorId(id);
            boolean puedeCambiarTipo = conceptoService.puedeCambiarTipo(id);
            prepararFormulario(model, concepto, "Editar concepto", "/conceptos/actualizar/" + id, true, puedeCambiarTipo);
            return "conceptos/formulario";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/conceptos";
        }
    }

    @PostMapping("/conceptos/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @ModelAttribute Concepto concepto,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            conceptoService.actualizar(id, concepto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Concepto actualizado correctamente.");
            return "redirect:/conceptos";
        } catch (RuntimeException exception) {
            boolean puedeCambiarTipo = false;
            try {
                puedeCambiarTipo = conceptoService.puedeCambiarTipo(id);
            } catch (RuntimeException ignored) {
                puedeCambiarTipo = false;
            }
            prepararFormulario(model, concepto, "Editar concepto", "/conceptos/actualizar/" + id, true, puedeCambiarTipo);
            model.addAttribute("mensajeError", exception.getMessage());
            return "conceptos/formulario";
        }
    }

    @GetMapping("/conceptos/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ConceptoResumenDTO concepto = conceptoService.obtenerResumenPorId(id);
            model.addAttribute("concepto", concepto);
            model.addAttribute("ultimasTransacciones", conceptoService.obtenerUltimasTransacciones(id));
            model.addAttribute("activePage", "conceptos");
            return "conceptos/detalle";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/conceptos";
        }
    }

    @PostMapping("/conceptos/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Concepto concepto = conceptoService.cambiarEstado(id);
            String mensaje = Boolean.TRUE.equals(concepto.getActivo())
                    ? "Concepto activado correctamente."
                    : "Concepto desactivado correctamente.";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
        }
        return "redirect:/conceptos";
    }

    private void prepararFormulario(
            Model model,
            Concepto concepto,
            String titulo,
            String action,
            boolean mostrarActivo,
            boolean puedeCambiarTipo) {
        model.addAttribute("concepto", concepto);
        model.addAttribute("tituloFormulario", titulo);
        model.addAttribute("formAction", action);
        model.addAttribute("mostrarActivo", mostrarActivo);
        model.addAttribute("puedeCambiarTipo", puedeCambiarTipo);
        model.addAttribute("tipos", TipoConcepto.values());
        model.addAttribute("activePage", "conceptos");
    }

    private boolean hayFiltros(TipoConcepto tipo, Boolean activo, String buscar) {
        return tipo != null || activo != null || (buscar != null && !buscar.trim().isEmpty());
    }
}
