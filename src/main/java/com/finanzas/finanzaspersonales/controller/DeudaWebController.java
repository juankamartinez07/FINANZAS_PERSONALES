package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.dto.DeudaResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenDeudasDTO;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import com.finanzas.finanzaspersonales.entity.Deuda;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.service.DeudaService;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class DeudaWebController {

    private final DeudaService deudaService;
    private final CuentaRepository cuentaRepository;

    public DeudaWebController(DeudaService deudaService, CuentaRepository cuentaRepository) {
        this.deudaService = deudaService;
        this.cuentaRepository = cuentaRepository;
    }

    @GetMapping("/deudas")
    public String listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer cuenta,
            @RequestParam(required = false) String buscar,
            Model model) {
        List<DeudaResumenDTO> deudas = deudaService.listarResumen(estado, cuenta, buscar);
        ResumenDeudasDTO resumen = deudaService.obtenerResumenFiltrado(deudas);
        model.addAttribute("deudas", deudas);
        model.addAttribute("resumen", resumen);
        model.addAttribute("cuentas", cuentaRepository.findByActivoTrue());
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("cuentaSeleccionada", cuenta);
        model.addAttribute("buscar", buscar);
        model.addAttribute("hayFiltros", hayFiltros(estado, cuenta, buscar));
        model.addAttribute("activePage", "deudas");
        return "deudas/index";
    }

    @GetMapping("/deudas/nueva")
    public String nueva(Model model) {
        Deuda deuda = new Deuda();
        deuda.setActivo(true);
        deuda.setFechaInicio(LocalDate.now());
        deuda.setSaldoActual(BigDecimal.ZERO);
        deuda.setCuenta(new Cuenta());
        prepararFormulario(model, deuda, "Nueva deuda", "/deudas/guardar", false);
        return "deudas/formulario";
    }

    @PostMapping("/deudas/guardar")
    public String guardar(
            @ModelAttribute Deuda deuda,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            deudaService.guardar(deuda);
            redirectAttributes.addFlashAttribute("mensajeExito", "Deuda registrada correctamente.");
            if (deuda.getSaldoActual() != null && deuda.getSaldoActual().compareTo(BigDecimal.ZERO) == 0) {
                redirectAttributes.addFlashAttribute("mensajeInfo", "La deuda ha sido pagada completamente.");
            }
            return "redirect:/deudas";
        } catch (RuntimeException exception) {
            prepararFormulario(model, deuda, "Nueva deuda", "/deudas/guardar", false);
            model.addAttribute("mensajeError", exception.getMessage());
            return "deudas/formulario";
        }
    }

    @GetMapping("/deudas/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Deuda deuda = deudaService.buscarPorId(id);
            prepararFormulario(model, deuda, "Editar deuda", "/deudas/actualizar/" + id, true);
            return "deudas/formulario";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/deudas";
        }
    }

    @PostMapping("/deudas/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @ModelAttribute Deuda deuda,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Deuda deudaActualizada = deudaService.actualizar(id, deuda);
            redirectAttributes.addFlashAttribute("mensajeExito", "Deuda actualizada correctamente.");
            if (deudaActualizada.getSaldoActual() != null
                    && deudaActualizada.getSaldoActual().compareTo(BigDecimal.ZERO) == 0) {
                redirectAttributes.addFlashAttribute("mensajeInfo", "La deuda ha sido pagada completamente.");
            }
            return "redirect:/deudas";
        } catch (RuntimeException exception) {
            prepararFormulario(model, deuda, "Editar deuda", "/deudas/actualizar/" + id, true);
            model.addAttribute("mensajeError", exception.getMessage());
            return "deudas/formulario";
        }
    }

    @GetMapping("/deudas/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            DeudaResumenDTO deuda = deudaService.obtenerResumenPorId(id);
            model.addAttribute("deuda", deuda);
            model.addAttribute("activePage", "deudas");
            return "deudas/detalle";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/deudas";
        }
    }

    @PostMapping("/deudas/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Deuda deuda = deudaService.cambiarEstado(id);
            String mensaje = Boolean.TRUE.equals(deuda.getActivo())
                    ? "Deuda activada correctamente."
                    : "Deuda desactivada correctamente.";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
        }
        return "redirect:/deudas";
    }

    private void prepararFormulario(Model model, Deuda deuda, String titulo, String action, boolean mostrarActivo) {
        if (deuda.getCuenta() == null) {
            deuda.setCuenta(new Cuenta());
        }
        model.addAttribute("deuda", deuda);
        model.addAttribute("cuentas", cuentaRepository.findByActivoTrue());
        model.addAttribute("tituloFormulario", titulo);
        model.addAttribute("formAction", action);
        model.addAttribute("mostrarActivo", mostrarActivo);
        model.addAttribute("activePage", "deudas");
    }

    private boolean hayFiltros(String estado, Integer cuenta, String buscar) {
        return cuenta != null
                || (estado != null && !estado.trim().isEmpty() && !"TODAS".equalsIgnoreCase(estado))
                || (buscar != null && !buscar.trim().isEmpty());
    }
}
