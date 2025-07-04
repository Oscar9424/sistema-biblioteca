package com.biblioteca.jira.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private EstadoTarea estado = EstadoTarea.PENDIENTE;
    
    @Enumerated(EnumType.STRING)
    private PrioridadTarea prioridad = PrioridadTarea.MEDIA;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_asignado_id")
    private Usuario asignadoA;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id", nullable = false)
    private Usuario creadoPor;
    
    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();
    
    // Métodos de utilidad para la relación bidireccional
    public void agregarComentario(Comentario comentario) {
        comentarios.add(comentario);
        comentario.setTarea(this);
    }
    
    public void eliminarComentario(Comentario comentario) {
        comentarios.remove(comentario);
        comentario.setTarea(null);
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public enum EstadoTarea {
        PENDIENTE, EN_PROGRESO, EN_REVISION, COMPLETADA, CERRADA
    }
    
    public enum PrioridadTarea {
        BAJA, MEDIA, ALTA, CRITICA
    }
}
