package com.biblioteca.jira.repository;

import com.biblioteca.jira.model.Comentario;
import com.biblioteca.jira.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByTareaOrderByFechaCreacionDesc(Tarea tarea);
    List<Comentario> findByAutorIdOrderByFechaCreacionDesc(Long autorId);
}
