package com.bovinelink.bovinelinkapi.service;

import com.bovinelink.bovinelinkapi.entity.Raza;
import com.bovinelink.bovinelinkapi.repository.RazaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RazaService {

    private final RazaRepository razaRepository;

    public List<Raza> listar() {
        // Orden por id: la migracion V8 asigna los ids en el orden en que
        // deben verse en el sidebar (por categoria y dentro de cada una).
        return razaRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Raza obtenerPorId(Long id) {
        return razaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Raza no encontrada"));
    }

    public Raza crear(Raza raza) {
        return razaRepository.save(raza);
    }

    public Raza actualizar(Long id, Raza request) {
        Raza raza = obtenerPorId(id);
        raza.setNombre(request.getNombre());
        raza.setCategoria(request.getCategoria());
        return razaRepository.save(raza);
    }

    public void eliminar(Long id) {
        Raza raza = obtenerPorId(id);
        razaRepository.delete(raza);
    }
}
