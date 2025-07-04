package com.biblioteca.jira.controller;

import com.biblioteca.jira.dto.ProyectoDTO;
import com.biblioteca.jira.service.ProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@Tag(name = "Proyectos", description = "API para la gestión de proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Autowired
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los proyectos")
    public ResponseEntity<List<ProyectoDTO>> obtenerTodosLosProyectos() {
        return ResponseEntity.ok(proyectoService.obtenerTodosLosProyectos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un proyecto por su ID")
    public ResponseEntity<ProyectoDTO> obtenerProyectoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerProyectoPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo proyecto")
    public ResponseEntity<ProyectoDTO> crearProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) {
        return new ResponseEntity<>(
                proyectoService.crearProyecto(proyectoDTO), 
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un proyecto existente")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(
            @PathVariable Long id, 
            @Valid @RequestBody ProyectoDTO proyectoDTO) {
        return ResponseEntity.ok(proyectoService.actualizarProyecto(id, proyectoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un proyecto")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{proyectoId}/usuarios/{usuarioId}")
    @Operation(summary = "Agregar un usuario a un proyecto")
    public ResponseEntity<Void> agregarUsuarioAProyecto(
            @PathVariable Long proyectoId,
            @PathVariable Long usuarioId) {
        proyectoService.agregarUsuarioAProyecto(proyectoId, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{proyectoId}/usuarios/{usuarioId}")
    @Operation(summary = "Eliminar un usuario de un proyecto")
    public ResponseEntity<Void> eliminarUsuarioDeProyecto(
            @PathVariable Long proyectoId,
            @PathVariable Long usuarioId) {
        proyectoService.eliminarUsuarioDeProyecto(proyectoId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
