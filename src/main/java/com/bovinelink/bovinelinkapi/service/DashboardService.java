package com.bovinelink.bovinelinkapi.service;

import com.bovinelink.bovinelinkapi.dto.ConteoResponse;
import com.bovinelink.bovinelinkapi.dto.DashboardResponse;
import com.bovinelink.bovinelinkapi.entity.Publicacion;
import com.bovinelink.bovinelinkapi.repository.EstadisticaRepository;
import com.bovinelink.bovinelinkapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final EstadisticaRepository estadisticaRepository;
    private final UsuarioRepository usuarioRepository;

    public DashboardResponse obtenerResumen() {
        long totalPublicaciones = estadisticaRepository.count();
        long usuariosRegistrados = usuarioRepository.count();
        long ganadoVendido = estadisticaRepository.countByEstado(Publicacion.Estado.VENDIDO);
        long publicacionesEnVenta = estadisticaRepository.countByEstado(Publicacion.Estado.EN_VENTA);
        BigDecimal montoTotalVendido = estadisticaRepository.sumarPrecioPorEstado(Publicacion.Estado.VENDIDO);

        return new DashboardResponse(
                totalPublicaciones,
                usuariosRegistrados,
                ganadoVendido,
                publicacionesEnVenta,
                montoTotalVendido);
    }

    public List<ConteoResponse> publicacionesPorUbicacion() {
        return estadisticaRepository.contarPorUbicacion();
    }

    public List<ConteoResponse> publicacionesPorRaza() {
        return estadisticaRepository.contarPorRaza();
    }
}
