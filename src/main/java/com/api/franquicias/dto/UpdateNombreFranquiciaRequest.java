package com.api.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNombreFranquiciaRequest {

    @NotBlank(message = "El nuevo nombre de la franquicia es obligatorio")
    private String nuevoNombre;
}
