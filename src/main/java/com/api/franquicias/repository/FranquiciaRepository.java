package com.api.franquicias.repository;

import com.api.franquicias.model.Franquicia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranquiciaRepository extends JpaRepository<Franquicia, Long> {
    Optional<Franquicia> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
