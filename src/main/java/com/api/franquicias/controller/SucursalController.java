package com.api.franquicias.controller;

import com.api.franquicias.dto.SucursalRequest;
import com.api.franquicias.dto.UpdateNombreSucursalRequest;
import com.api.franquicias.model.Sucursal;
import com.api.franquicias.service.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    public ResponseEntity<Sucursal> crearSucursal(@Valid @RequestBody SucursalRequest request) {
        Sucursal sucursal = sucursalService.crearSucursal(request);
        return new ResponseEntity<>(sucursal, HttpStatus.CREATED);
    }

    @GetMapping("/franquicia/{franquiciaId}")
    public ResponseEntity<List<Sucursal>> obtenerSucursalesPorFranquicia(@PathVariable Long franquiciaId) {
        List<Sucursal> sucursales = sucursalService.obtenerSucursalesPorFranquicia(franquiciaId);
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerSucursalPorId(@PathVariable Long id) {
        Sucursal sucursal = sucursalService.obtenerSucursalPorId(id);
        return ResponseEntity.ok(sucursal);
    }

    @PatchMapping("/{id}/nombre")
    public ResponseEntity<Sucursal> actualizarNombre(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNombreSucursalRequest request) {
        Sucursal sucursal = sucursalService.actualizarNombre(id, request);
        return ResponseEntity.ok(sucursal);
    }
}
