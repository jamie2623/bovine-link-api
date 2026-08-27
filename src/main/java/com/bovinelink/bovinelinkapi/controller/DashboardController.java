package com.bovinelink.bovinelinkapi.controller;

import com.bovinelink.bovinelinkapi.dto.ConteoResponse;
import com.bovinelink.bovinelinkapi.dto.DashboardResponse;
import com.bovinelink.bovinelinkapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResponse> resumen() {
        return ResponseEntity.ok(dashboardService.obtenerResumen());
    }

    @GetMapping("/por-ubicacion")
    public ResponseEntity<List<ConteoResponse>> porUbicacion() {
        return ResponseEntity.ok(dashboardService.publicacionesPorUbicacion());
    }

    @GetMapping("/por-raza")
    public ResponseEntity<List<ConteoResponse>> porRaza() {
        return ResponseEntity.ok(dashboardService.publicacionesPorRaza());
    }
}
