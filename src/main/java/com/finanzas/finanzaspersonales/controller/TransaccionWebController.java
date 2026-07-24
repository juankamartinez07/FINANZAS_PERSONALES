package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.dto.ResumenTransaccionesDTO;
import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.repository.ConceptoRepository;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.service.TransaccionService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransaccionWebController {

    private final TransaccionService transaccionService;
    private final CuentaRepository cuentaRepository;
    private final ConceptoRepository conceptoRepository;

    public TransaccionWebController(
            TransaccionService transaccionService,
            CuentaRepository cuentaRepository,
            ConceptoRepository conceptoRepository) {
        this.transaccionService = transaccionService;
        this.cuentaRepository = cuentaRepository;
        this.conceptoRepository = conceptoRepository;
    }

    @GetMapping("/transacciones")
    public String listar(
            @RequestParam(required = false) TipoConcepto tipo,
            @RequestParam(required = false) Integer cuenta,
            @RequestParam(required = false) Integer concepto,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String buscar,
            Model model) {
        List<Transaccion> transacciones = transaccionService.listarConFiltros(
                tipo,
                cuenta,
                concepto,
                desde,
                hasta,
                buscar);
        ResumenTransaccionesDTO resumen = transaccionService.obtenerResumenConFiltros(
                tipo,
                cuenta,
                concepto,
                desde,
                hasta,
                buscar);

        cargarCatalogos(model);
        model.addAttribute("transacciones", transacciones);
        model.addAttribute("resumen", resumen);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("cuentaSeleccionada", cuenta);
        model.addAttribute("conceptoSeleccionado", concepto);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("buscar", buscar);
        model.addAttribute("hayFiltros", hayFiltros(tipo, cuenta, concepto, desde, hasta, buscar));
        model.addAttribute("activePage", "transacciones");
        return "transacciones/index";
    }

    @GetMapping("/transacciones/nueva")
    public String nueva(Model model) {
        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(LocalDate.now());
        prepararFormulario(model, transaccion, "Nueva transaccion", "/transacciones/guardar");
        return "transacciones/formulario";
    }

    @PostMapping("/transacciones/guardar")
    public String guardar(
            @ModelAttribute Transaccion transaccion,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            transaccionService.guardar(transaccion);
            redirectAttributes.addFlashAttribute("mensajeExito", "Transaccion registrada correctamente.");
            return "redirect:/transacciones";
        } catch (RuntimeException exception) {
            prepararFormulario(model, transaccion, "Nueva transaccion", "/transacciones/guardar");
            model.addAttribute("mensajeError", exception.getMessage());
            return "transacciones/formulario";
        }
    }

    @GetMapping("/transacciones/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Transaccion transaccion = transaccionService.buscarPorId(id);
            prepararFormulario(model, transaccion, "Editar transaccion", "/transacciones/actualizar/" + id);
            return "transacciones/formulario";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/transacciones";
        }
    }

    @PostMapping("/transacciones/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @ModelAttribute Transaccion transaccion,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            transaccionService.actualizar(id, transaccion);
            redirectAttributes.addFlashAttribute("mensajeExito", "Transaccion actualizada correctamente.");
            return "redirect:/transacciones";
        } catch (RuntimeException exception) {
            prepararFormulario(model, transaccion, "Editar transaccion", "/transacciones/actualizar/" + id);
            model.addAttribute("mensajeError", exception.getMessage());
            return "transacciones/formulario";
        }
    }

    @PostMapping("/transacciones/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            transaccionService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Transaccion eliminada correctamente.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
        }
        return "redirect:/transacciones";
    }

    private void prepararFormulario(Model model, Transaccion transaccion, String titulo, String action) {
        cargarCatalogos(model);
        model.addAttribute("transaccion", transaccion);
        model.addAttribute("tituloFormulario", titulo);
        model.addAttribute("formAction", action);
        model.addAttribute("activePage", "transacciones");
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("cuentas", cuentaRepository.findByActivoTrue());
        model.addAttribute("conceptos", conceptoRepository.findByActivoTrue());
        model.addAttribute("tipos", TipoConcepto.values());
    }

    private boolean hayFiltros(
            TipoConcepto tipo,
            Integer cuenta,
            Integer concepto,
            LocalDate desde,
            LocalDate hasta,
            String buscar) {
        return tipo != null
                || cuenta != null
                || concepto != null
                || desde != null
                || hasta != null
                || (buscar != null && !buscar.trim().isEmpty());
    }
}
