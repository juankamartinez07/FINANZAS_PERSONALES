package com.finanzas.finanzaspersonales.controller;

import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.service.ConceptoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conceptos")
public class ConceptoController {

    private final ConceptoService conceptoService;

    public ConceptoController(ConceptoService conceptoService) {
        this.conceptoService = conceptoService;
    }

    @GetMapping
    public ResponseEntity<List<Concepto>> listar() {
        return ResponseEntity.ok(conceptoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concepto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(conceptoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Concepto> guardar(@RequestBody Concepto concepto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conceptoService.guardar(concepto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concepto> actualizar(@PathVariable Integer id, @RequestBody Concepto concepto) {
        return ResponseEntity.ok(conceptoService.actualizar(id, concepto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        conceptoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
