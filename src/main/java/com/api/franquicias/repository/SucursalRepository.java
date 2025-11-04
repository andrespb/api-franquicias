package com.api.franquicias.repository;

import com.api.franquicias.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    List<Sucursal> findByFranquiciaId(Long franquiciaId);

    @Query("SELECT s FROM Sucursal s LEFT JOIN FETCH s.productos WHERE s.franquicia.id = :franquiciaId")
    List<Sucursal> findByFranquiciaIdWithProductos(@Param("franquiciaId") Long franquiciaId);
}
