package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.entity.Deuda;
import com.finanzas.finanzaspersonales.service.DeudaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deudas")
public class DeudaController {

    private final DeudaService deudaService;

    public DeudaController(DeudaService deudaService) {
        this.deudaService = deudaService;
    }

    @GetMapping
    public ResponseEntity<List<Deuda>> listar() {
        return ResponseEntity.ok(deudaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deuda> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(deudaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Deuda> guardar(@RequestBody Deuda deuda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deudaService.guardar(deuda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deuda> actualizar(@PathVariable Integer id, @RequestBody Deuda deuda) {
        return ResponseEntity.ok(deudaService.actualizar(id, deuda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        deudaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Deuda>> listarActivas() {
        return ResponseEntity.ok(deudaService.listarActivas());
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<Deuda>> listarPorCuenta(@PathVariable Integer idCuenta) {
        return ResponseEntity.ok(deudaService.listarPorCuenta(idCuenta));
    }

    @GetMapping("/entidad/{entidad}")
    public ResponseEntity<List<Deuda>> listarPorEntidad(@PathVariable String entidad) {
        return ResponseEntity.ok(deudaService.listarPorEntidad(entidad));
    }
}
