package com.api.franquicias.service;

import com.api.franquicias.dto.ProductoRequest;
import com.api.franquicias.dto.UpdateNombreProductoRequest;
import com.api.franquicias.dto.UpdateStockRequest;
import com.api.franquicias.model.Producto;
import com.api.franquicias.model.Sucursal;
import com.api.franquicias.repository.ProductoRepository;
import com.api.franquicias.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    @Transactional
    @CacheEvict(value = { "productos", "sucursales", "franquicias" }, allEntries = true)
    public Producto crearProducto(ProductoRequest request) {
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + request.getSucursalId()));

        Producto producto = new Producto(request.getNombre(), request.getStock());
        sucursal.addProducto(producto);

        return productoRepository.save(producto);
    }

    @Transactional
    @CacheEvict(value = { "productos", "sucursales", "franquicias" }, allEntries = true)
    public void eliminarProducto(Long sucursalId, Long productoId) {
        Producto producto = productoRepository.findByIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId +
                        " en la sucursal: " + sucursalId));

        productoRepository.delete(producto);
    }

    @Transactional
    @CacheEvict(value = { "productos", "sucursales", "franquicias" }, allEntries = true)
    public Producto actualizarStock(Long productoId, UpdateStockRequest request) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));

        producto.setStock(request.getNuevoStock());
        return productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "productos", key = "'sucursal-' + #sucursalId")
    public List<Producto> obtenerProductosPorSucursal(Long sucursalId) {
        return productoRepository.findBySucursalId(sucursalId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "productos", key = "#id")
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Transactional
    @CacheEvict(value = { "productos", "sucursales", "franquicias" }, allEntries = true)
    public Producto actualizarNombre(Long id, UpdateNombreProductoRequest request) {
        Producto producto = obtenerProductoPorId(id);
        producto.setNombre(request.getNuevoNombre());
        return productoRepository.save(producto);
    }
}
