package com.biblioteca.jira.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String avatarUrl;
    
    @ManyToMany(mappedBy = "usuarios")
    private List<Proyecto> proyectos = new ArrayList<>();
    
    @OneToMany(mappedBy = "asignadoA")
    private List<Tarea> tareasAsignadas = new ArrayList<>();
    
    @OneToMany(mappedBy = "creadoPor")
    private List<Tarea> tareasCreadas = new ArrayList<>();
    
    @OneToMany(mappedBy = "autor")
    private List<Comentario> comentarios = new ArrayList<>();
    
    // Métodos de utilidad para la relación bidireccional
    public void agregarProyecto(Proyecto proyecto) {
        proyectos.add(proyecto);
        proyecto.getUsuarios().add(this);
    }
    
    public void eliminarProyecto(Proyecto proyecto) {
        proyectos.remove(proyecto);
        proyecto.getUsuarios().remove(this);
    }
}
