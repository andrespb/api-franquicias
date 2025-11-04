package com.api.franquicias.service;

import com.api.franquicias.dto.FranquiciaRequest;
import com.api.franquicias.dto.ProductoMaxStockDTO;
import com.api.franquicias.dto.UpdateNombreFranquiciaRequest;
import com.api.franquicias.model.Franquicia;
import com.api.franquicias.model.Producto;
import com.api.franquicias.model.Sucursal;
import com.api.franquicias.repository.FranquiciaRepository;
import com.api.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;

    @Transactional
    @CacheEvict(value = "franquicias", allEntries = true)
    public Franquicia crearFranquicia(FranquiciaRequest request) {
        if (franquiciaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una franquicia con el nombre: " + request.getNombre());
        }

        Franquicia franquicia = new Franquicia(request.getNombre());
        return franquiciaRepository.save(franquicia);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "franquicias", key = "'all'")
    public List<Franquicia> obtenerTodasLasFranquicias() {
        return franquiciaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "franquicias", key = "#id")
    public Franquicia obtenerFranquiciaPorId(Long id) {
        return franquiciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductoMaxStockDTO> obtenerProductosConMaxStockPorSucursal(Long franquiciaId) {
        Franquicia franquicia = obtenerFranquiciaPorId(franquiciaId);

        List<Sucursal> sucursales = sucursalRepository.findByFranquiciaIdWithProductos(franquiciaId);
        List<ProductoMaxStockDTO> resultado = new ArrayList<>();

        for (Sucursal sucursal : sucursales) {
            if (!sucursal.getProductos().isEmpty()) {
                Producto productoMaxStock = sucursal.getProductos().stream()
                        .max(Comparator.comparing(Producto::getStock))
                        .orElse(null);

                if (productoMaxStock != null) {
                    ProductoMaxStockDTO dto = new ProductoMaxStockDTO(
                            productoMaxStock.getId(),
                            productoMaxStock.getNombre(),
                            productoMaxStock.getStock(),
                            sucursal.getId(),
                            sucursal.getNombre());
                    resultado.add(dto);
                }
            }
        }

        return resultado;
    }

    @Transactional
    @CacheEvict(value = "franquicias", allEntries = true)
    public Franquicia actualizarNombre(Long id, UpdateNombreFranquiciaRequest request) {
        Franquicia franquicia = obtenerFranquiciaPorId(id);

        // Verificar que el nuevo nombre no esté en uso por otra franquicia
        if (franquiciaRepository.existsByNombre(request.getNuevoNombre())
                && !franquicia.getNombre().equals(request.getNuevoNombre())) {
            throw new RuntimeException("Ya existe una franquicia con el nombre: " + request.getNuevoNombre());
        }

        franquicia.setNombre(request.getNuevoNombre());
        return franquiciaRepository.save(franquicia);
    }
}
