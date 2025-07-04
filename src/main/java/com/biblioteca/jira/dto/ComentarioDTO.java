package com.biblioteca.jira.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioDTO {
    private Long id;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private Long tareaId;
    private Long autorId;
    private String autorNombre;
}
