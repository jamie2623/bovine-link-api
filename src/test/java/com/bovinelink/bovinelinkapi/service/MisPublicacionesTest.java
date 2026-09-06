package com.bovinelink.bovinelinkapi.service;

import com.bovinelink.bovinelinkapi.dto.PublicacionRequest;
import com.bovinelink.bovinelinkapi.entity.Publicacion;
import com.bovinelink.bovinelinkapi.entity.Usuario;
import com.bovinelink.bovinelinkapi.repository.PublicacionRepository;
import com.bovinelink.bovinelinkapi.repository.RazaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MisPublicacionesTest {

    @Test void consultaSoloElUsuarioAutenticado() {
        var repo = mock(PublicacionRepository.class);
        var servicio = new PublicacionService(repo, mock(RazaRepository.class));
        var usuario = new Usuario();
        usuario.setId(42L);
        when(repo.findByUsuarioIdOrderByFechaPublicacionDesc(42L)).thenReturn(List.of());

        assertTrue(servicio.listarMias(usuario).isEmpty());
        verify(repo).findByUsuarioIdOrderByFechaPublicacionDesc(42L);
        verify(repo, never()).findAll();
    }

    @Test void impideEditarYEliminarPublicacionesAjenas() {
        var repo = mock(PublicacionRepository.class);
        var servicio = new PublicacionService(repo, mock(RazaRepository.class));
        var propietario = new Usuario();
        propietario.setId(1L);
        var otro = new Usuario();
        otro.setId(2L);
        var publicacion = new Publicacion();
        publicacion.setUsuario(propietario);
        when(repo.findById(5L)).thenReturn(Optional.of(publicacion));

        assertThrows(IllegalArgumentException.class, () -> servicio.eliminar(5L, otro));
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizar(5L, new PublicacionRequest(), otro));
        verify(repo, never()).delete(any());
        verify(repo, never()).save(any());
    }
}
