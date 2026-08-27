package com.bovinelink.bovinelinkapi.controller;

import com.bovinelink.bovinelinkapi.dto.ReporteFiltroRequest;
import com.bovinelink.bovinelinkapi.dto.ReporteItemResponse;
import com.bovinelink.bovinelinkapi.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    /** Tabla filtrada: GET /api/reportes?desde=&hasta=&estado= */
    @GetMapping
    public ResponseEntity<List<ReporteItemResponse>> listar(@ModelAttribute ReporteFiltroRequest filtro) {
        return ResponseEntity.ok(reporteService.generarReporte(filtro));
    }

    /** Mismo reporte en PDF: GET /api/reportes/exportar-pdf?desde=&hasta=&estado= */
    @GetMapping("/exportar-pdf")
    public ResponseEntity<byte[]> exportarPdf(@ModelAttribute ReporteFiltroRequest filtro) {
        byte[] pdf = reporteService.exportarPdf(filtro);
        String nombreArchivo = "reporte-publicaciones-" + LocalDate.now() + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nombreArchivo + "\"")
                .body(pdf);
    }
}
