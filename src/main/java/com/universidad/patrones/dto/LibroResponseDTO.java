package com.universidad.patrones.dto;
import lombok.*;
// DTO de salida — datos que retorna la API
@Data @NoArgsConstructor @AllArgsConstructor
public class LibroResponseDTO {
 private Long id;
 private String titulo;
 private String autor;
 private String isbn;
 private Integer anioPublicacion;
 private String categoria;
}