package com.biblioteca.jira.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String clave;
    
    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarea> tareas = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "proyecto_usuarios",
        joinColumns = @JoinColumn(name = "proyecto_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios = new ArrayList<>();
    
    // Métodos de utilidad para la relación bidireccional
    public void agregarTarea(Tarea tarea) {
        tareas.add(tarea);
        tarea.setProyecto(this);
    }
    
    public void eliminarTarea(Tarea tarea) {
        tareas.remove(tarea);
        tarea.setProyecto(null);
    }
    
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        usuario.getProyectos().add(this);
    }
    
    public void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
        usuario.getProyectos().remove(this);
    }
}
