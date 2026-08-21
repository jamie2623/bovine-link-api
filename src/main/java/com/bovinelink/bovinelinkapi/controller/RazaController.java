package com.bovinelink.bovinelinkapi.controller;

import com.bovinelink.bovinelinkapi.entity.Raza;
import com.bovinelink.bovinelinkapi.service.RazaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/razas")
@RequiredArgsConstructor
public class RazaController {

    private final RazaService razaService;

    @GetMapping
    public ResponseEntity<List<Raza>> listar() {
        return ResponseEntity.ok(razaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Raza> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(razaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Raza> crear(@Valid @RequestBody Raza raza) {
        return ResponseEntity.ok(razaService.crear(raza));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Raza> actualizar(@PathVariable Long id, @Valid @RequestBody Raza raza) {
        return ResponseEntity.ok(razaService.actualizar(id, raza));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        razaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
