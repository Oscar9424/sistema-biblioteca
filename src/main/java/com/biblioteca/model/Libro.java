package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String titulo;
    
    @Column(nullable = false, length = 50)
    private String autor;
    
    @Column(unique = true, nullable = false, length = 20)
    private String isbn;
    
    @Column(name = "anio_publicacion", nullable = false)
    private Integer anioPublicacion;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;
    
    @Column(length = 50)
    private String editorial;
    
    @Column(length = 50)
    private String genero;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_creacion", updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;
    
    // Método para verificar disponibilidad
    public boolean estaDisponible() {
        return cantidadDisponible > 0;
    }
    
    // Método para registrar un préstamo
    public void prestar() {
        if (cantidadDisponible > 0) {
            cantidadDisponible--;
        }
    }
    
    // Método para registrar una devolución
    public void devolver() {
        if (cantidadDisponible < cantidad) {
            cantidadDisponible++;
        }
    }
}
