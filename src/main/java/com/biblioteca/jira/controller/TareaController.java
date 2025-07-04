package com.biblioteca.jira.controller;

import com.biblioteca.jira.dto.TareaDTO;
import com.biblioteca.jira.model.Tarea;
import com.biblioteca.jira.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@Tag(name = "Tareas", description = "API para la gestión de tareas")
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las tareas")
    public ResponseEntity<List<TareaDTO>> obtenerTodasLasTareas() {
        return ResponseEntity.ok(tareaService.obtenerTodasLasTareas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tarea por su ID")
    public ResponseEntity<TareaDTO> obtenerTareaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerTareaPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva tarea")
    public ResponseEntity<TareaDTO> crearTarea(@Valid @RequestBody TareaDTO tareaDTO) {
        return new ResponseEntity<>(
                tareaService.crearTarea(tareaDTO),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea existente")
    public ResponseEntity<TareaDTO> actualizarTarea(
            @PathVariable Long id,
            @Valid @RequestBody TareaDTO tareaDTO) {
        return ResponseEntity.ok(tareaService.actualizarTarea(id, tareaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/proyecto/{proyectoId}")
    @Operation(summary = "Obtener tareas por proyecto")
    public ResponseEntity<List<TareaDTO>> obtenerTareasPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(tareaService.obtenerTareasPorProyecto(proyectoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener tareas asignadas a un usuario")
    public ResponseEntity<List<TareaDTO>> obtenerTareasPorUsuarioAsignado(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tareaService.obtenerTareasPorUsuarioAsignado(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener tareas por estado")
    public ResponseEntity<List<TareaDTO>> obtenerTareasPorEstado(@PathVariable Tarea.EstadoTarea estado) {
        return ResponseEntity.ok(tareaService.obtenerTareasPorEstado(estado));
    }

    @GetMapping("/prioridad/{prioridad}")
    @Operation(summary = "Obtener tareas por prioridad")
    public ResponseEntity<List<TareaDTO>> obtenerTareasPorPrioridad(@PathVariable Tarea.PrioridadTarea prioridad) {
        return ResponseEntity.ok(tareaService.obtenerTareasPorPrioridad(prioridad));
    }
}
