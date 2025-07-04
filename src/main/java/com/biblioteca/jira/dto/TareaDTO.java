package com.biblioteca.jira.dto;

import com.biblioteca.jira.model.Tarea;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TareaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Tarea.EstadoTarea estado;
    private Tarea.PrioridadTarea prioridad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Long proyectoId;
    private Long asignadoAId;
    private Long creadoPorId;
}
