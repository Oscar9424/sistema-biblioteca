package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestamo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;
    
    @Column(name = "fecha_devolucion_prevista", nullable = false)
    private LocalDate fechaDevolucionPrevista;
    
    @Column(name = "fecha_devolucion_real")
    private LocalDate fechaDevolucionReal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    // Método para verificar si el préstamo está activo
    public boolean estaActivo() {
        return estado == EstadoPrestamo.ACTIVO;
    }
    
    // Método para registrar la devolución
    public void registrarDevolucion(String observaciones) {
        this.estado = EstadoPrestamo.DEVUELTO;
        this.fechaDevolucionReal = LocalDate.now();
        this.observaciones = observaciones;
        
        // Actualizar la disponibilidad del libro
        if (libro != null) {
            libro.devolver();
        }
    }
    
    // Método para verificar si el préstamo está vencido
    public boolean estaVencido() {
        return estaActivo() && LocalDate.now().isAfter(fechaDevolucionPrevista);
    }
    
    // Enumeración para los estados del préstamo
    public enum EstadoPrestamo {
        ACTIVO,
        DEVUELTO,
        VENCIDO,
        PERDIDO
    }
}
