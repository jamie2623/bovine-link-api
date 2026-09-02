package com.bovinelink.bovinelinkapi.service;

import com.bovinelink.bovinelinkapi.dto.AuthResponse;
import com.bovinelink.bovinelinkapi.dto.LoginRequest;
import com.bovinelink.bovinelinkapi.dto.RegisterRequest;
import com.bovinelink.bovinelinkapi.entity.Rol;
import com.bovinelink.bovinelinkapi.entity.Usuario;
import com.bovinelink.bovinelinkapi.repository.RolRepository;
import com.bovinelink.bovinelinkapi.repository.UsuarioRepository;
import com.bovinelink.bovinelinkapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("Ese correo ya está registrado");
        }

        Rol rolUsuario = rolRepository.findByName(Rol.USUARIO)
                .orElseThrow(() -> new IllegalStateException("El rol '" + Rol.USUARIO + "' no existe en la base de datos"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rolUsuario);

        usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(usuario.getCorreo());
        return new AuthResponse(token, usuario.getId(), usuario.getNombre(),
                usuario.getCorreo(), usuario.getRol().getName());
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos");
        }

        String token = jwtUtil.generarToken(usuario.getCorreo());
        return new AuthResponse(token, usuario.getId(), usuario.getNombre(),
                usuario.getCorreo(), usuario.getRol().getName());
    }
}
