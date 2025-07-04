package com.biblioteca.jira.service.impl;

import com.biblioteca.jira.dto.TareaDTO;
import com.biblioteca.jira.exception.ResourceNotFoundException;
import com.biblioteca.jira.model.*;
import com.biblioteca.jira.repository.ProyectoRepository;
import com.biblioteca.jira.repository.TareaRepository;
import com.biblioteca.jira.repository.UsuarioRepository;
import com.biblioteca.jira.service.TareaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TareaServiceImpl(TareaRepository tareaRepository,
                           ProyectoRepository proyectoRepository,
                           UsuarioRepository usuarioRepository,
                           ModelMapper modelMapper) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TareaDTO> obtenerTodasLasTareas() {
        return tareaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TareaDTO obtenerTareaPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        return convertirADTO(tarea);
    }

    @Override
    @Transactional
    public TareaDTO crearTarea(TareaDTO tareaDTO) {
        Tarea tarea = convertirAEntidad(tareaDTO);
        
        // Establecer relaciones
        Proyecto proyecto = proyectoRepository.findById(tareaDTO.getProyectoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + tareaDTO.getProyectoId()));
        tarea.setProyecto(proyecto);
        
        Usuario creadoPor = usuarioRepository.findById(tareaDTO.getCreadoPorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado con id: " + tareaDTO.getCreadoPorId()));
        tarea.setCreadoPor(creadoPor);
        
        if (tareaDTO.getAsignadoAId() != null) {
            Usuario asignadoA = usuarioRepository.findById(tareaDTO.getAsignadoAId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario asignado no encontrado con id: " + tareaDTO.getAsignadoAId()));
            tarea.setAsignadoA(asignadoA);
        }
        
        Tarea tareaGuardada = tareaRepository.save(tarea);
        return convertirADTO(tareaGuardada);
    }

    @Override
    @Transactional
    public TareaDTO actualizarTarea(Long id, TareaDTO tareaDTO) {
        Tarea tareaExistente = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        
        // Actualizar campos básicos
        tareaExistente.setTitulo(tareaDTO.getTitulo());
        tareaExistente.setDescripcion(tareaDTO.getDescripcion());
        tareaExistente.setEstado(tareaDTO.getEstado());
        tareaExistente.setPrioridad(tareaDTO.getPrioridad());
        
        // Actualizar relaciones si es necesario
        if (tareaDTO.getAsignadoAId() != null && 
            (tareaExistente.getAsignadoA() == null || 
             !tareaExistente.getAsignadoA().getId().equals(tareaDTO.getAsignadoAId()))) {
            
            Usuario asignadoA = usuarioRepository.findById(tareaDTO.getAsignadoAId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario asignado no encontrado con id: " + tareaDTO.getAsignadoAId()));
            tareaExistente.setAsignadoA(asignadoA);
        }
        
        Tarea tareaActualizada = tareaRepository.save(tareaExistente);
        return convertirADTO(tareaActualizada);
    }

    @Override
    @Transactional
    public void eliminarTarea(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        tareaRepository.delete(tarea);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TareaDTO> obtenerTareasPorProyecto(Long proyectoId) {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(proyectoId);
        return tareaRepository.findByProyecto(proyecto).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TareaDTO> obtenerTareasPorUsuarioAsignado(Long usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        return tareaRepository.findByAsignadoA(usuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TareaDTO> obtenerTareasPorEstado(Tarea.EstadoTarea estado) {
        return tareaRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TareaDTO> obtenerTareasPorPrioridad(Tarea.PrioridadTarea prioridad) {
        return tareaRepository.findByPrioridad(prioridad).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private TareaDTO convertirADTO(Tarea tarea) {
        TareaDTO dto = modelMapper.map(tarea, TareaDTO.class);
        // Mapear manualmente los IDs de las relaciones
        if (tarea.getProyecto() != null) {
            dto.setProyectoId(tarea.getProyecto().getId());
        }
        if (tarea.getAsignadoA() != null) {
            dto.setAsignadoAId(tarea.getAsignadoA().getId());
        }
        if (tarea.getCreadoPor() != null) {
            dto.setCreadoPorId(tarea.getCreadoPor().getId());
        }
        return dto;
    }

    private Tarea convertirAEntidad(TareaDTO dto) {
        return modelMapper.map(dto, Tarea.class);
    }
}
