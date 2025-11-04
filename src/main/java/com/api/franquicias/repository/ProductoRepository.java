package com.api.franquicias.repository;

import com.api.franquicias.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySucursalId(Long sucursalId);

    @Query("SELECT p FROM Producto p WHERE p.sucursal.id = :sucursalId ORDER BY p.stock DESC")
    List<Producto> findBySucursalIdOrderByStockDesc(@Param("sucursalId") Long sucursalId);

    Optional<Producto> findByIdAndSucursalId(Long id, Long sucursalId);
}
