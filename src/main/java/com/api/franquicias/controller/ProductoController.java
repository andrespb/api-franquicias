package com.api.franquicias.controller;

import com.api.franquicias.dto.ProductoRequest;
import com.api.franquicias.dto.UpdateNombreProductoRequest;
import com.api.franquicias.dto.UpdateStockRequest;
import com.api.franquicias.model.Producto;
import com.api.franquicias.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoRequest request) {
        Producto producto = productoService.crearProducto(request);
        return new ResponseEntity<>(producto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productoId}/sucursal/{sucursalId}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long sucursalId,
            @PathVariable Long productoId) {
        productoService.eliminarProducto(sucursalId, productoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockRequest request) {
        Producto producto = productoService.actualizarStock(id, request);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Producto>> obtenerProductosPorSucursal(@PathVariable Long sucursalId) {
        List<Producto> productos = productoService.obtenerProductosPorSucursal(sucursalId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PatchMapping("/{id}/nombre")
    public ResponseEntity<Producto> actualizarNombre(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNombreProductoRequest request) {
        Producto producto = productoService.actualizarNombre(id, request);
        return ResponseEntity.ok(producto);
    }
}
