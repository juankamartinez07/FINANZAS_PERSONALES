package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.dto.CuentaDetalleDTO;
import com.finanzas.finanzaspersonales.dto.CuentaResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenCuentasDTO;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import com.finanzas.finanzaspersonales.service.CuentaService;
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
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/cuentas")
    public String listar(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) Boolean activo,
            Model model) {
        List<CuentaResumenDTO> cuentas = cuentaService.listarResumen(buscar, activo);
        ResumenCuentasDTO resumen = cuentaService.obtenerResumenGeneral(cuentas);
        model.addAttribute("cuentas", cuentas);
        model.addAttribute("resumen", resumen);
        model.addAttribute("buscar", buscar);
        model.addAttribute("activoSeleccionado", activo);
        model.addAttribute("hayFiltros", hayFiltros(buscar, activo));
        model.addAttribute("activePage", "cuentas");
        return "cuentas/index";
    }

    @GetMapping("/cuentas/nueva")
    public String nueva(Model model) {
        prepararFormulario(model, new Cuenta(), "Nueva cuenta", "/cuentas/guardar", false);
        return "cuentas/formulario";
    }

    @PostMapping("/cuentas/guardar")
    public String guardar(
            @ModelAttribute Cuenta cuenta,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            cuentaService.guardar(cuenta);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cuenta creada correctamente.");
            return "redirect:/cuentas";
        } catch (RuntimeException exception) {
            prepararFormulario(model, cuenta, "Nueva cuenta", "/cuentas/guardar", false);
            model.addAttribute("mensajeError", exception.getMessage());
            return "cuentas/formulario";
        }
    }

    @GetMapping("/cuentas/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cuenta cuenta = cuentaService.buscarPorId(id);
            prepararFormulario(model, cuenta, "Editar cuenta", "/cuentas/actualizar/" + id, true);
            return "cuentas/formulario";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/cuentas";
        }
    }

    @PostMapping("/cuentas/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @ModelAttribute Cuenta cuenta,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            cuentaService.actualizar(id, cuenta);
            redirectAttributes.addFlashAttribute("mensajeExito", "Cuenta actualizada correctamente.");
            return "redirect:/cuentas";
        } catch (RuntimeException exception) {
            prepararFormulario(model, cuenta, "Editar cuenta", "/cuentas/actualizar/" + id, true);
            model.addAttribute("mensajeError", exception.getMessage());
            return "cuentas/formulario";
        }
    }

    @PostMapping("/cuentas/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Cuenta cuenta = cuentaService.cambiarEstado(id);
            String mensaje = Boolean.TRUE.equals(cuenta.getActivo())
                    ? "Cuenta activada correctamente."
                    : "Cuenta desactivada correctamente.";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", "No es posible completar la operacion.");
        }
        return "redirect:/cuentas";
    }

    @GetMapping("/cuentas/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CuentaDetalleDTO detalle = cuentaService.obtenerDetalle(id);
            model.addAttribute("detalle", detalle);
            model.addAttribute("activePage", "cuentas");
            return "cuentas/detalle";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/cuentas";
        }
    }

    @GetMapping("/cuentas/{id}/transacciones")
    public String transacciones(@PathVariable Integer id) {
        return "redirect:/transacciones?cuenta=" + id;
    }

    private void prepararFormulario(Model model, Cuenta cuenta, String titulo, String action, boolean mostrarActivo) {
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("tituloFormulario", titulo);
        model.addAttribute("formAction", action);
        model.addAttribute("mostrarActivo", mostrarActivo);
        model.addAttribute("activePage", "cuentas");
    }

    private boolean hayFiltros(String buscar, Boolean activo) {
        return activo != null || (buscar != null && !buscar.trim().isEmpty());
    }
}
