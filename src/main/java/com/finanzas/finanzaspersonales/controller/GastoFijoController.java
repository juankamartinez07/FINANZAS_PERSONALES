package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.entity.GastoFijo;
import com.finanzas.finanzaspersonales.service.GastoFijoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gastosfijos")
public class GastoFijoController {

    private final GastoFijoService gastoFijoService;

    public GastoFijoController(GastoFijoService gastoFijoService) {
        this.gastoFijoService = gastoFijoService;
    }

    @GetMapping
    public ResponseEntity<List<GastoFijo>> listar() {
        return ResponseEntity.ok(gastoFijoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoFijo> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(gastoFijoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<GastoFijo> guardar(@RequestBody GastoFijo gastoFijo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gastoFijoService.guardar(gastoFijo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoFijo> actualizar(@PathVariable Integer id, @RequestBody GastoFijo gastoFijo) {
        return ResponseEntity.ok(gastoFijoService.actualizar(id, gastoFijo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        gastoFijoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<GastoFijo>> listarActivos() {
        return ResponseEntity.ok(gastoFijoService.listarActivos());
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<GastoFijo>> listarPorCuenta(@PathVariable Integer idCuenta) {
        return ResponseEntity.ok(gastoFijoService.listarPorCuenta(idCuenta));
    }

    @GetMapping("/dia-pago/{diaPago}")
    public ResponseEntity<List<GastoFijo>> listarPorDiaPago(@PathVariable Integer diaPago) {
        return ResponseEntity.ok(gastoFijoService.listarPorDiaPago(diaPago));
    }
}
