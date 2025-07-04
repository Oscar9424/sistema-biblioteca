package com.biblioteca.jira.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProyectoDTO {
    private Long id;
    private String clave;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Set<Long> usuarioIds = new HashSet<>();
}
