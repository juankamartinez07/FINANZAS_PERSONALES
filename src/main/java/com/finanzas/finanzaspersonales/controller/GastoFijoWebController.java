package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.dto.GastoFijoResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenGastosFijosDTO;
import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import com.finanzas.finanzaspersonales.entity.GastoFijo;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.repository.ConceptoRepository;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.service.GastoFijoService;
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
public class GastoFijoWebController {

    private final GastoFijoService gastoFijoService;
    private final CuentaRepository cuentaRepository;
    private final ConceptoRepository conceptoRepository;

    public GastoFijoWebController(
            GastoFijoService gastoFijoService,
            CuentaRepository cuentaRepository,
            ConceptoRepository conceptoRepository) {
        this.gastoFijoService = gastoFijoService;
        this.cuentaRepository = cuentaRepository;
        this.conceptoRepository = conceptoRepository;
    }

    @GetMapping("/gastosfijos")
    public String listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer cuenta,
            @RequestParam(required = false) Integer concepto,
            @RequestParam(required = false) String buscar,
            Model model) {
        List<GastoFijoResumenDTO> gastosFijos = gastoFijoService.listarResumen(estado, cuenta, concepto, buscar);
        ResumenGastosFijosDTO resumen = gastoFijoService.obtenerResumenFiltrado(gastosFijos);
        model.addAttribute("gastosFijos", gastosFijos);
        model.addAttribute("resumen", resumen);
        model.addAttribute("cuentas", cuentaRepository.findByActivoTrue());
        model.addAttribute("conceptos", conceptoRepository.findByActivoTrueAndTipo(TipoConcepto.GASTO));
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("cuentaSeleccionada", cuenta);
        model.addAttribute("conceptoSeleccionado", concepto);
        model.addAttribute("buscar", buscar);
        model.addAttribute("hayFiltros", hayFiltros(estado, cuenta, concepto, buscar));
        model.addAttribute("activePage", "gastosfijos");
        return "gastosfijos/index";
    }

    @GetMapping("/gastosfijos/nuevo")
    public String nuevo(Model model) {
        GastoFijo gastoFijo = new GastoFijo();
        gastoFijo.setActivo(true);
        gastoFijo.setCuenta(new Cuenta());
        gastoFijo.setConcepto(new Concepto());
        prepararFormulario(model, gastoFijo, "Nuevo gasto fijo", "/gastosfijos/guardar", false);
        return "gastosfijos/formulario";
    }

    @PostMapping("/gastosfijos/guardar")
    public String guardar(
            @ModelAttribute GastoFijo gastoFijo,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            gastoFijoService.guardar(gastoFijo);
            redirectAttributes.addFlashAttribute("mensajeExito", "Gasto fijo creado correctamente.");
            return "redirect:/gastosfijos";
        } catch (RuntimeException exception) {
            prepararFormulario(model, gastoFijo, "Nuevo gasto fijo", "/gastosfijos/guardar", false);
            model.addAttribute("mensajeError", exception.getMessage());
            return "gastosfijos/formulario";
        }
    }

    @GetMapping("/gastosfijos/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            GastoFijo gastoFijo = gastoFijoService.buscarPorId(id);
            prepararFormulario(model, gastoFijo, "Editar gasto fijo", "/gastosfijos/actualizar/" + id, true);
            return "gastosfijos/formulario";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/gastosfijos";
        }
    }

    @PostMapping("/gastosfijos/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @ModelAttribute GastoFijo gastoFijo,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            gastoFijoService.actualizar(id, gastoFijo);
            redirectAttributes.addFlashAttribute("mensajeExito", "Gasto fijo actualizado correctamente.");
            return "redirect:/gastosfijos";
        } catch (RuntimeException exception) {
            prepararFormulario(model, gastoFijo, "Editar gasto fijo", "/gastosfijos/actualizar/" + id, true);
            model.addAttribute("mensajeError", exception.getMessage());
            return "gastosfijos/formulario";
        }
    }

    @GetMapping("/gastosfijos/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            GastoFijoResumenDTO gastoFijo = gastoFijoService.obtenerResumenPorId(id);
            model.addAttribute("gastoFijo", gastoFijo);
            model.addAttribute("activePage", "gastosfijos");
            return "gastosfijos/detalle";
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
            return "redirect:/gastosfijos";
        }
    }

    @PostMapping("/gastosfijos/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            GastoFijo gastoFijo = gastoFijoService.cambiarEstado(id);
            String mensaje = Boolean.TRUE.equals(gastoFijo.getActivo())
                    ? "Gasto fijo activado correctamente."
                    : "Gasto fijo desactivado correctamente.";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("mensajeError", exception.getMessage());
        }
        return "redirect:/gastosfijos";
    }

    private void prepararFormulario(Model model, GastoFijo gastoFijo, String titulo, String action, boolean mostrarActivo) {
        if (gastoFijo.getCuenta() == null) {
            gastoFijo.setCuenta(new Cuenta());
        }
        if (gastoFijo.getConcepto() == null) {
            gastoFijo.setConcepto(new Concepto());
        }
        model.addAttribute("gastoFijo", gastoFijo);
        model.addAttribute("cuentas", cuentaRepository.findByActivoTrue());
        model.addAttribute("conceptos", conceptoRepository.findByActivoTrueAndTipo(TipoConcepto.GASTO));
        model.addAttribute("tituloFormulario", titulo);
        model.addAttribute("formAction", action);
        model.addAttribute("mostrarActivo", mostrarActivo);
        model.addAttribute("activePage", "gastosfijos");
    }

    private boolean hayFiltros(String estado, Integer cuenta, Integer concepto, String buscar) {
        return cuenta != null
                || concepto != null
                || (estado != null && !estado.trim().isEmpty() && !"TODOS".equalsIgnoreCase(estado))
                || (buscar != null && !buscar.trim().isEmpty());
    }
}
