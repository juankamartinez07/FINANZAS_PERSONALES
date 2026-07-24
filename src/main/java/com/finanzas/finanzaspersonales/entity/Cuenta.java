package com.finanzas.finanzaspersonales.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuenta;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Boolean activo = true;

    public Cuenta() {
    }

    public Cuenta(Integer idCuenta, String nombre, Boolean activo) {
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cuenta cuenta)) {
            return false;
        }
        return Objects.equals(idCuenta, cuenta.idCuenta)
                && Objects.equals(nombre, cuenta.nombre)
                && Objects.equals(activo, cuenta.activo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCuenta, nombre, activo);
    }

    @Override
    public String toString() {
        return "Cuenta{"
                + "idCuenta=" + idCuenta
                + ", nombre='" + nombre + '\''
                + ", activo=" + activo
                + '}';
    }
}
