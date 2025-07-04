package com.biblioteca.jira.repository;

import com.biblioteca.jira.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    Optional<Proyecto> findByClave(String clave);
    boolean existsByClave(String clave);
}
