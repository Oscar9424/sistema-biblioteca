package com.biblioteca.jira.service;

import com.biblioteca.jira.dto.TareaDTO;
import com.biblioteca.jira.model.Tarea;
import java.util.List;

public interface TareaService {
    List<TareaDTO> obtenerTodasLasTareas();
    TareaDTO obtenerTareaPorId(Long id);
    TareaDTO crearTarea(TareaDTO tareaDTO);
    TareaDTO actualizarTarea(Long id, TareaDTO tareaDTO);
    void eliminarTarea(Long id);
    List<TareaDTO> obtenerTareasPorProyecto(Long proyectoId);
    List<TareaDTO> obtenerTareasPorUsuarioAsignado(Long usuarioId);
    List<TareaDTO> obtenerTareasPorEstado(Tarea.EstadoTarea estado);
    List<TareaDTO> obtenerTareasPorPrioridad(Tarea.PrioridadTarea prioridad);
}
