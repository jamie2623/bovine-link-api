package com.bovinelink.bovinelinkapi.service;

import com.bovinelink.bovinelinkapi.dto.ReporteFiltroRequest;
import com.bovinelink.bovinelinkapi.dto.ReporteItemResponse;
import com.bovinelink.bovinelinkapi.repository.EstadisticaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteService {

    private final EstadisticaRepository estadisticaRepository;
    private final PdfService pdfService;

    /** Tabla filtrada para GET /api/reportes. */
    public List<ReporteItemResponse> generarReporte(ReporteFiltroRequest filtro) {
        validarRango(filtro);
        return estadisticaRepository.buscarReporte(
                filtro.desdeComoInicioDelDia(),
                filtro.hastaComoFinDelDia(),
                filtro.estadoComoEnum());
    }

    /** Mismo reporte en PDF para GET /api/reportes/exportar-pdf. */
    public byte[] exportarPdf(ReporteFiltroRequest filtro) {
        List<ReporteItemResponse> filas = generarReporte(filtro);
        return pdfService.generarReporteDePublicaciones(filas, filtro);
    }

    private void validarRango(ReporteFiltroRequest filtro) {
        if (filtro.getDesde() != null && filtro.getHasta() != null
                && filtro.getHasta().isBefore(filtro.getDesde())) {
            throw new IllegalArgumentException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }
    }
}
