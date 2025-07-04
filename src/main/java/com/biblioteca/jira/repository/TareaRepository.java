package com.biblioteca.jira.repository;

import com.biblioteca.jira.model.Proyecto;
import com.biblioteca.jira.model.Tarea;
import com.biblioteca.jira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByProyecto(Proyecto proyecto);
    List<Tarea> findByAsignadoA(Usuario usuario);
    List<Tarea> findByCreadoPor(Usuario usuario);
    List<Tarea> findByProyectoAndEstado(Proyecto proyecto, Tarea.EstadoTarea estado);
    List<Tarea> findByProyectoAndPrioridad(Proyecto proyecto, Tarea.PrioridadTarea prioridad);
}
