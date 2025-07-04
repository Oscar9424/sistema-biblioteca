package com.biblioteca.jira.service;

import com.biblioteca.jira.dto.UsuarioDTO;
import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> obtenerTodosLosUsuarios();
    UsuarioDTO obtenerUsuarioPorId(Long id);
    UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);
    void eliminarUsuario(Long id);
    UsuarioDTO obtenerUsuarioPorEmail(String email);
    UsuarioDTO obtenerUsuarioPorUsername(String username);
}
