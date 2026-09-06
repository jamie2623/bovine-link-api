package com.bovinelink.bovinelinkapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FotoStorageService {
    private final Path directorio;

    public FotoStorageService(@Value("${app.fotos.directorio:uploads/publicaciones}") String directorio) {
        this.directorio = Path.of(directorio).toAbsolutePath().normalize();
    }

    public List<String> guardar(List<MultipartFile> archivos) throws IOException {
        if (archivos == null) return List.of();
        if (archivos.size() > 5) throw new IllegalArgumentException("Se permiten como máximo 5 fotos");
        List<String> urls = new ArrayList<>();
        try {
            for (MultipartFile archivo : archivos) {
                if (archivo.isEmpty() || archivo.getSize() > 5 * 1024 * 1024)
                    throw new IllegalArgumentException("Cada foto debe pesar entre 1 byte y 5 MB");
                String formato;
                try (var entrada = archivo.getInputStream(); ImageInputStream imagen = ImageIO.createImageInputStream(entrada)) {
                    var lectores = ImageIO.getImageReaders(imagen);
                    if (!lectores.hasNext()) throw new IllegalArgumentException("Archivo de imagen inválido");
                    var lector = lectores.next();
                    try {
                        formato = lector.getFormatName().toLowerCase(java.util.Locale.ROOT);
                        if (!List.of("jpeg", "png").contains(formato)) throw new IllegalArgumentException("Solo se permiten fotos JPG o PNG");
                        lector.setInput(imagen);
                        if ((long) lector.getWidth(0) * lector.getHeight(0) > 25_000_000)
                            throw new IllegalArgumentException("La foto supera el límite de 25 megapíxeles");
                        lector.read(0);
                    } finally { lector.dispose(); }
                }
                Files.createDirectories(directorio);
                String nombre = UUID.randomUUID() + (formato.equals("jpeg") ? ".jpg" : ".png");
                String url = "/api/publicaciones/fotos/" + nombre;
                urls.add(url);
                try (var entrada = archivo.getInputStream()) { Files.copy(entrada, directorio.resolve(nombre)); }
            }
            return urls;
        } catch (IOException | RuntimeException e) {
            try { eliminar(urls); } catch (IOException limpieza) { e.addSuppressed(limpieza); }
            throw e;
        }
    }

    public void eliminar(List<String> urls) throws IOException {
        for (String url : urls) {
            String nombre = url.substring(url.lastIndexOf('/') + 1);
            Files.deleteIfExists(ruta(nombre));
        }
    }

    private Path ruta(String nombre) {
        if (!nombre.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.(jpg|png)"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return directorio.resolve(nombre);
    }

    public Resource leer(String nombre) throws IOException {
        Path archivo = ruta(nombre);
        if (!Files.isRegularFile(archivo)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new UrlResource(archivo.toUri());
    }
}
