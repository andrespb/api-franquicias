package com.api.franquicias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaxStockDTO {
    private Long productoId;
    private String productoNombre;
    private Integer stock;
    private Long sucursalId;
    private String sucursalNombre;
}
