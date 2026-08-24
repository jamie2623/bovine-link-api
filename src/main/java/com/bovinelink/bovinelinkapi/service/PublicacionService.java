package com.bovinelink.bovinelinkapi.service;

import com.bovinelink.bovinelinkapi.dto.PublicacionRequest;
import com.bovinelink.bovinelinkapi.dto.PublicacionResponse;
import com.bovinelink.bovinelinkapi.entity.FotoPublicacion;
import com.bovinelink.bovinelinkapi.entity.Publicacion;
import com.bovinelink.bovinelinkapi.entity.Raza;
import com.bovinelink.bovinelinkapi.entity.Usuario;
import com.bovinelink.bovinelinkapi.repository.PublicacionRepository;
import com.bovinelink.bovinelinkapi.repository.RazaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final RazaRepository razaRepository;

    @Transactional(readOnly = true)
    public List<PublicacionResponse> listar() {
        return publicacionRepository.findAll().stream()
                .map(PublicacionResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PublicacionResponse obtenerPorId(Long id) {
        return PublicacionResponse.fromEntity(buscarPorId(id));
    }

    @Transactional
    public PublicacionResponse crear(PublicacionRequest request, Usuario usuario) {
        Raza raza = buscarRaza(request.getRazaId());

        Publicacion publicacion = new Publicacion();
        aplicarDatos(publicacion, request, raza);
        publicacion.setUsuario(usuario);

        return PublicacionResponse.fromEntity(publicacionRepository.save(publicacion));
    }

    @Transactional
    public PublicacionResponse actualizar(Long id, PublicacionRequest request, Usuario usuario) {
        Publicacion publicacion = buscarPorId(id);
        validarPropietario(publicacion, usuario);

        Raza raza = buscarRaza(request.getRazaId());
        aplicarDatos(publicacion, request, raza);

        return PublicacionResponse.fromEntity(publicacionRepository.save(publicacion));
    }

    @Transactional
    public void eliminar(Long id, Usuario usuario) {
        Publicacion publicacion = buscarPorId(id);
        validarPropietario(publicacion, usuario);
        publicacionRepository.delete(publicacion);
    }

    private void aplicarDatos(Publicacion publicacion, PublicacionRequest request, Raza raza) {
        publicacion.setTitulo(request.getTitulo());
        publicacion.setDescripcion(request.getDescripcion());
        publicacion.setPrecio(request.getPrecio());
        publicacion.setUbicacion(request.getUbicacion());
        publicacion.setEstado(request.getEstado() == null ? Publicacion.Estado.EN_VENTA : request.getEstado());
        publicacion.setRaza(raza);

        publicacion.getFotos().clear();
        if (request.getFotos() != null) {
            request.getFotos().stream()
                    .filter(url -> url != null && !url.isBlank())
                    .forEach(url -> {
                        FotoPublicacion foto = new FotoPublicacion();
                        foto.setUrl(url);
                        foto.setPublicacion(publicacion);
                        publicacion.getFotos().add(foto);
                    });
        }
    }

    private Publicacion buscarPorId(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
    }

    private Raza buscarRaza(Long razaId) {
        return razaRepository.findById(razaId)
                .orElseThrow(() -> new IllegalArgumentException("Raza no encontrada"));
    }

    private void validarPropietario(Publicacion publicacion, Usuario usuario) {
        boolean esPropietario = publicacion.getUsuario().getId().equals(usuario.getId());
        boolean esAdmin = usuario.getRol() == Usuario.Rol.ADMIN;

        if (!esPropietario && !esAdmin) {
            throw new IllegalArgumentException("No puedes modificar esta publicacion");
        }
    }
}
