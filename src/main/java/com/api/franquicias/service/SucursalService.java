package com.api.franquicias.service;

import com.api.franquicias.dto.SucursalRequest;
import com.api.franquicias.dto.UpdateNombreSucursalRequest;
import com.api.franquicias.model.Franquicia;
import com.api.franquicias.model.Sucursal;
import com.api.franquicias.repository.FranquiciaRepository;
import com.api.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    @Transactional
    @CacheEvict(value = { "sucursales", "franquicias" }, allEntries = true)
    public Sucursal crearSucursal(SucursalRequest request) {
        Franquicia franquicia = franquiciaRepository.findById(request.getFranquiciaId())
                .orElseThrow(
                        () -> new RuntimeException("Franquicia no encontrada con id: " + request.getFranquiciaId()));

        Sucursal sucursal = new Sucursal(request.getNombre());
        franquicia.addSucursal(sucursal);

        return sucursalRepository.save(sucursal);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "sucursales", key = "'franquicia-' + #franquiciaId")
    public List<Sucursal> obtenerSucursalesPorFranquicia(Long franquiciaId) {
        return sucursalRepository.findByFranquiciaId(franquiciaId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "sucursales", key = "#id")
    public Sucursal obtenerSucursalPorId(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + id));
    }

    @Transactional
    @CacheEvict(value = { "sucursales", "franquicias" }, allEntries = true)
    public Sucursal actualizarNombre(Long id, UpdateNombreSucursalRequest request) {
        Sucursal sucursal = obtenerSucursalPorId(id);
        sucursal.setNombre(request.getNuevoNombre());
        return sucursalRepository.save(sucursal);
    }
}
