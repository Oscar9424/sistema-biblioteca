package com.biblioteca.jira.service.impl;

import com.biblioteca.jira.dto.ProyectoDTO;
import com.biblioteca.jira.exception.ResourceNotFoundException;
import com.biblioteca.jira.model.Proyecto;
import com.biblioteca.jira.model.Usuario;
import com.biblioteca.jira.repository.ProyectoRepository;
import com.biblioteca.jira.repository.UsuarioRepository;
import com.biblioteca.jira.service.ProyectoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProyectoServiceImpl(ProyectoRepository proyectoRepository, 
                             UsuarioRepository usuarioRepository,
                             ModelMapper modelMapper) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoDTO> obtenerTodosLosProyectos() {
        return proyectoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDTO obtenerProyectoPorId(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + id));
        return convertirADTO(proyecto);
    }

    @Override
    @Transactional
    public ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO) {
        // Verificar si ya existe un proyecto con la misma clave
        if (proyectoRepository.existsByClave(proyectoDTO.getClave())) {
            throw new RuntimeException("Ya existe un proyecto con la clave: " + proyectoDTO.getClave());
        }

        Proyecto proyecto = convertirAEntidad(proyectoDTO);
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);
        
        // Asignar usuarios al proyecto si se proporcionaron
        if (proyectoDTO.getUsuarioIds() != null && !proyectoDTO.getUsuarioIds().isEmpty()) {
            proyectoDTO.getUsuarioIds().forEach(usuarioId -> 
                agregarUsuarioAProyecto(proyectoGuardado.getId(), usuarioId)
            );
        }
        
        return convertirADTO(proyectoGuardado);
    }

    @Override
    @Transactional
    public ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO) {
        Proyecto proyectoExistente = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + id));

        // Actualizar campos básicos
        proyectoExistente.setNombre(proyectoDTO.getNombre());
        proyectoExistente.setDescripcion(proyectoDTO.getDescripcion());
        
        // Actualizar usuarios del proyecto
        if (proyectoDTO.getUsuarioIds() != null) {
            // Limpiar usuarios actuales
            proyectoExistente.getUsuarios().clear();
            // Agregar los nuevos usuarios
            proyectoDTO.getUsuarioIds().forEach(usuarioId -> 
                agregarUsuarioAProyecto(id, usuarioId)
            );
        }

        Proyecto proyectoActualizado = proyectoRepository.save(proyectoExistente);
        return convertirADTO(proyectoActualizado);
    }

    @Override
    @Transactional
    public void eliminarProyecto(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + id));
        proyectoRepository.delete(proyecto);
    }

    @Override
    @Transactional
    public void agregarUsuarioAProyecto(Long proyectoId, Long usuarioId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + proyectoId));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        
        proyecto.agregarUsuario(usuario);
        proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional
    public void eliminarUsuarioDeProyecto(Long proyectoId, Long usuarioId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + proyectoId));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        
        proyecto.eliminarUsuario(usuario);
        proyectoRepository.save(proyecto);
    }

    private ProyectoDTO convertirADTO(Proyecto proyecto) {
        ProyectoDTO dto = modelMapper.map(proyecto, ProyectoDTO.class);
        // Mapear manualmente los IDs de usuarios
        if (proyecto.getUsuarios() != null) {
            dto.setUsuarioIds(
                proyecto.getUsuarios().stream()
                    .map(Usuario::getId)
                    .collect(Collectors.toSet())
            );
        }
        return dto;
    }

    private Proyecto convertirAEntidad(ProyectoDTO dto) {
        return modelMapper.map(dto, Proyecto.class);
    }
}
