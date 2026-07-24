package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.entity.Ahorro;
import com.finanzas.finanzaspersonales.service.AhorroService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ahorros")
public class AhorroController {

    private final AhorroService ahorroService;

    public AhorroController(AhorroService ahorroService) {
        this.ahorroService = ahorroService;
    }

    @GetMapping
    public ResponseEntity<List<Ahorro>> listar() {
        return ResponseEntity.ok(ahorroService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ahorro> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ahorroService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Ahorro> guardar(@RequestBody Ahorro ahorro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ahorroService.guardar(ahorro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ahorro> actualizar(@PathVariable Integer id, @RequestBody Ahorro ahorro) {
        return ResponseEntity.ok(ahorroService.actualizar(id, ahorro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ahorroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Ahorro>> listarActivos() {
        return ResponseEntity.ok(ahorroService.listarActivos());
    }
}
