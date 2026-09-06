package com.bovinelink.bovinelinkapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import static org.junit.jupiter.api.Assertions.*;

class FotoStorageServiceTest {
    @TempDir Path directorio;

    private MockMultipartFile imagen() throws Exception {
        var bytes = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB), "png", bytes);
        return new MockMultipartFile("fotos", "../../animal.png", "image/png", bytes.toByteArray());
    }

    @Test void guardaLeeYEliminaSinUsarElNombreDelCliente() throws Exception {
        var storage = new FotoStorageService(directorio.toString());
        var urls = storage.guardar(List.of(imagen()));
        String nombre = urls.get(0).substring(urls.get(0).lastIndexOf('/') + 1);
        assertTrue(storage.leer(nombre).exists());
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> storage.leer("../secreto"));
        storage.eliminar(urls);
        try (var archivos = Files.list(directorio)) { assertEquals(0, archivos.count()); }
    }

    @Test void rechazaArchivosDisfrazadosYRevierteFotosAnteriores() throws Exception {
        var storage = new FotoStorageService(directorio.toString());
        var falso = new MockMultipartFile("fotos", "falso.png", "image/png", "no es una imagen".getBytes());
        assertThrows(IllegalArgumentException.class, () -> storage.guardar(List.of(imagen(), falso)));
        try (var archivos = Files.list(directorio)) { assertEquals(0, archivos.count()); }
    }

    @Test void rechazaMasDeCincoFotos() throws Exception {
        var storage = new FotoStorageService(directorio.toString());
        var foto = imagen();
        assertThrows(IllegalArgumentException.class, () -> storage.guardar(List.of(foto, foto, foto, foto, foto, foto)));
    }
}
