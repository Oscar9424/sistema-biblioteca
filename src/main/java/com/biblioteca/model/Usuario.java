package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(nullable = false, length = 50)
    private String apellido;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(unique = true, length = 20)
    private String telefono;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(nullable = false, length = 200)
    private String direccion;
    
    @Column(name = "fecha_creacion", updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prestamo> prestamos = new ArrayList<>();
    
    // Método para verificar si el usuario tiene préstamos activos
    public boolean tienePrestamosActivos() {
        return prestamos.stream().anyMatch(Prestamo::estaActivo);
    }
    
    // Método para agregar un préstamo
    public void agregarPrestamo(Prestamo prestamo) {
        prestamos.add(prestamo);
        prestamo.setUsuario(this);
    }
    
    // Método para remover un préstamo
    public void removerPrestamo(Prestamo prestamo) {
        prestamos.remove(prestamo);
        prestamo.setUsuario(null);
    }
}
