package com.api.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNombreProductoRequest {

    @NotBlank(message = "El nuevo nombre del producto es obligatorio")
    private String nuevoNombre;
}
