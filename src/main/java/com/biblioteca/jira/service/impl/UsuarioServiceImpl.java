package com.biblioteca.jira.service.impl;

import com.biblioteca.jira.dto.UsuarioDTO;
import com.biblioteca.jira.exception.ResourceAlreadyExistsException;
import com.biblioteca.jira.exception.ResourceNotFoundException;
import com.biblioteca.jira.model.Usuario;
import com.biblioteca.jira.repository.UsuarioRepository;
import com.biblioteca.jira.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                            ModelMapper modelMapper,
                            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return convertirADTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        // Verificar si el email ya está en uso
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("El email ya está en uso: " + usuarioDTO.getEmail());
        }
        
        // Verificar si el nombre de usuario ya está en uso
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new ResourceAlreadyExistsException("El nombre de usuario ya está en uso: " + usuarioDTO.getUsername());
        }
        
        // Crear nuevo usuario
        Usuario usuario = convertirAEntidad(usuarioDTO);
        
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    @Override
    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        
        // Verificar si el nuevo email ya está en uso por otro usuario
        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(usuarioExistente.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                throw new ResourceAlreadyExistsException("El email ya está en uso: " + usuarioDTO.getEmail());
            }
            usuarioExistente.setEmail(usuarioDTO.getEmail());
        }
        
        // Verificar si el nuevo nombre de usuario ya está en uso por otro usuario
        if (usuarioDTO.getUsername() != null && !usuarioDTO.getUsername().equals(usuarioExistente.getUsername())) {
            if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
                throw new ResourceAlreadyExistsException("El nombre de usuario ya está en uso: " + usuarioDTO.getUsername());
            }
            usuarioExistente.setUsername(usuarioDTO.getUsername());
        }
        
        // Actualizar campos básicos
        if (usuarioDTO.getNombre() != null) {
            usuarioExistente.setNombre(usuarioDTO.getNombre());
        }
        
        if (usuarioDTO.getApellido() != null) {
            usuarioExistente.setApellido(usuarioDTO.getApellido());
        }
        
        if (usuarioDTO.getAvatarUrl() != null) {
            usuarioExistente.setAvatarUrl(usuarioDTO.getAvatarUrl());
        }
        
        // Si se proporciona una nueva contraseña, encriptarla
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return convertirADTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        
        // Verificar si el usuario tiene tareas asignadas o creadas
        if (!usuario.getTareasAsignadas().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el usuario porque tiene tareas asignadas");
        }
        
        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
        // No exponer la contraseña
        dto.setPassword(null);
        return dto;
    }

    private Usuario convertirAEntidad(UsuarioDTO dto) {
        return modelMapper.map(dto, Usuario.class);
    }
}
