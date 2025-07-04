package com.biblioteca.jira.service;

import com.biblioteca.jira.dto.ProyectoDTO;
import java.util.List;

public interface ProyectoService {
    List<ProyectoDTO> obtenerTodosLosProyectos();
    ProyectoDTO obtenerProyectoPorId(Long id);
    ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO);
    ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO);
    void eliminarProyecto(Long id);
    void agregarUsuarioAProyecto(Long proyectoId, Long usuarioId);
    void eliminarUsuarioDeProyecto(Long proyectoId, Long usuarioId);
}
