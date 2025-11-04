package com.api.franquicias.controller;

import com.api.franquicias.dto.FranquiciaRequest;
import com.api.franquicias.dto.ProductoMaxStockDTO;
import com.api.franquicias.dto.UpdateNombreFranquiciaRequest;
import com.api.franquicias.model.Franquicia;
import com.api.franquicias.service.FranquiciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @PostMapping
    public ResponseEntity<Franquicia> crearFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        Franquicia franquicia = franquiciaService.crearFranquicia(request);
        return new ResponseEntity<>(franquicia, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Franquicia>> obtenerTodasLasFranquicias() {
        List<Franquicia> franquicias = franquiciaService.obtenerTodasLasFranquicias();
        return ResponseEntity.ok(franquicias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Franquicia> obtenerFranquiciaPorId(@PathVariable Long id) {
        Franquicia franquicia = franquiciaService.obtenerFranquiciaPorId(id);
        return ResponseEntity.ok(franquicia);
    }

    @GetMapping("/{id}/productos-max-stock")
    public ResponseEntity<List<ProductoMaxStockDTO>> obtenerProductosConMaxStockPorSucursal(@PathVariable Long id) {
        List<ProductoMaxStockDTO> productos = franquiciaService.obtenerProductosConMaxStockPorSucursal(id);
        return ResponseEntity.ok(productos);
    }

    @PatchMapping("/{id}/nombre")
    public ResponseEntity<Franquicia> actualizarNombre(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNombreFranquiciaRequest request) {
        Franquicia franquicia = franquiciaService.actualizarNombre(id, request);
        return ResponseEntity.ok(franquicia);
    }
}
