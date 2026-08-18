package com.bovinelink.bovinelinkapi.controller;

import com.bovinelink.bovinelinkapi.dto.AuthResponse;
import com.bovinelink.bovinelinkapi.dto.LoginRequest;
import com.bovinelink.bovinelinkapi.dto.RegisterRequest;
import com.bovinelink.bovinelinkapi.entity.Usuario;
import com.bovinelink.bovinelinkapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> me(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(usuario);
    }
}