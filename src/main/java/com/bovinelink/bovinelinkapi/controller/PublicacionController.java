package com.bovinelink.bovinelinkapi.controller;

import com.bovinelink.bovinelinkapi.dto.PublicacionRequest;
import com.bovinelink.bovinelinkapi.dto.PublicacionResponse;
import com.bovinelink.bovinelinkapi.entity.Usuario;
import com.bovinelink.bovinelinkapi.service.FotoStorageService;
import com.bovinelink.bovinelinkapi.service.PublicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;
    private final FotoStorageService fotoStorageService;

    @GetMapping
    public ResponseEntity<List<PublicacionResponse>> listar() {
        return ResponseEntity.ok(publicacionService.listar());
    }

    /** Publicaciones del usuario autenticado. */
    @GetMapping("/mias")
    public ResponseEntity<List<PublicacionResponse>> mias(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(publicacionService.listarMias(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(publicacionService.obtenerPorId(id));
    }

    /** Crear con JSON (fotos como lista de URLs ya subidas, opcional). */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicacionResponse> crear(@Valid @RequestBody PublicacionRequest request,
                                                     @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(publicacionService.crear(request, usuario));
    }

    /** Crear con multipart: parte "publicacion" (JSON) + parte "fotos" (archivos). */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PublicacionResponse> crearConFotos(
            @Valid @RequestPart("publicacion") PublicacionRequest request,
            @RequestPart(value = "fotos", required = false) List<MultipartFile> fotos,
            @AuthenticationPrincipal Usuario usuario) throws IOException {
        List<String> urls = fotoStorageService.guardar(fotos);
        request.setFotos(urls);
        try {
            return ResponseEntity.ok(publicacionService.crear(request, usuario));
        } catch (RuntimeException e) {
            try {
                fotoStorageService.eliminar(urls);
            } catch (IOException limpieza) {
                e.addSuppressed(limpieza);
            }
            throw e;
        }
    }

    /** Sirve un archivo de foto guardado. */
    @GetMapping("/fotos/{nombre}")
    public ResponseEntity<Resource> foto(@PathVariable String nombre) throws IOException {
        Resource recurso = fotoStorageService.leer(nombre);
        return ResponseEntity.ok()
                .contentType(nombre.endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .header("X-Content-Type-Options", "nosniff")
                .body(recurso);
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
