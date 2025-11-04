package com.api.franquicias.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "franquicias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Franquicia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la franquicia es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "franquicia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Sucursal> sucursales = new ArrayList<>();

    public Franquicia(String nombre) {
        this.nombre = nombre;
    }

    public void addSucursal(Sucursal sucursal) {
        sucursales.add(sucursal);
        sucursal.setFranquicia(this);
    }

    public void removeSucursal(Sucursal sucursal) {
        sucursales.remove(sucursal);
        sucursal.setFranquicia(null);
    }
}
