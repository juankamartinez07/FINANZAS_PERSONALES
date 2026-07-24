package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.service.TransaccionService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaccion>> listar() {
        return ResponseEntity.ok(transaccionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(transaccionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Transaccion> guardar(@RequestBody Transaccion transaccion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.guardar(transaccion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaccion> actualizar(@PathVariable Integer id, @RequestBody Transaccion transaccion) {
        return ResponseEntity.ok(transaccionService.actualizar(id, transaccion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        transaccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<Transaccion>> listarPorCuenta(@PathVariable Integer idCuenta) {
        return ResponseEntity.ok(transaccionService.listarPorCuenta(idCuenta));
    }

    @GetMapping("/concepto/{idConcepto}")
    public ResponseEntity<List<Transaccion>> listarPorConcepto(@PathVariable Integer idConcepto) {
        return ResponseEntity.ok(transaccionService.listarPorConcepto(idConcepto));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Transaccion>> listarPorRangoFechas(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {
        return ResponseEntity.ok(transaccionService.listarPorRangoFechas(inicio, fin));
    }
}
