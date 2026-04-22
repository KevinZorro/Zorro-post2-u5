package com.universidad.patrones.controller;

import com.universidad.patrones.model.Libro;
import com.universidad.patrones.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.universidad.patrones.dto.*;
import com.universidad.patrones.mapper.LibroMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/libros")
@Tag(name = "Libros", description = "Operaciones CRUD sobre el catálogo de libros")
public class LibroController {
    private final LibroService service;
    private final LibroMapper mapper;

    public LibroController(LibroService service, LibroMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Listar todos los libros")
    public List<LibroResponseDTO> listar() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID")
    public ResponseEntity<LibroResponseDTO> obtener(@PathVariable Long id) {
        return service.findById(id)
                .map(mapper::toResponse).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo libro")
    public ResponseEntity<LibroResponseDTO> crear(@RequestBody @Valid LibroRequestDTO dto) {
        Libro guardado = service.save(mapper.toEntity(dto));
        return ResponseEntity.status(201).body(mapper.toResponse(guardado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}