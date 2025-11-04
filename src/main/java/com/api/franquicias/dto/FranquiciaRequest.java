package com.api.franquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranquiciaRequest {

    @NotBlank(message = "El nombre de la franquicia es obligatorio")
    private String nombre;
}
