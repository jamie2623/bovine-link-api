package com.bovinelink.bovinelinkapi.controller;

import com.bovinelink.bovinelinkapi.dto.PublicacionRequest;
import com.bovinelink.bovinelinkapi.dto.PublicacionResponse;
import com.bovinelink.bovinelinkapi.entity.Usuario;
import com.bovinelink.bovinelinkapi.service.PublicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    @GetMapping
    public ResponseEntity<List<PublicacionResponse>> listar() {
        return ResponseEntity.ok(publicacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(publicacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PublicacionResponse> crear(@Valid @RequestBody PublicacionRequest request,
                                                     @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(publicacionService.crear(request, usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicacionResponse> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody PublicacionRequest request,
                                                          @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(publicacionService.actualizar(id, request, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @AuthenticationPrincipal Usuario usuario) {
        publicacionService.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
