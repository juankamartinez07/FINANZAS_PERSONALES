package com.finanzas.finanzaspersonales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "ahorros")
public class Ahorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAhorro;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private BigDecimal meta;

    @Column(nullable = false)
    private BigDecimal ahorroActual;

    private Integer porcentajeRecomendado;

    @Column(nullable = false)
    private Boolean activo = true;

    public Ahorro() {
    }

    public Ahorro(
            Integer idAhorro,
            String nombre,
            String descripcion,
            BigDecimal meta,
            BigDecimal ahorroActual,
            Integer porcentajeRecomendado,
            Boolean activo) {
        this.idAhorro = idAhorro;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.meta = meta;
        this.ahorroActual = ahorroActual;
        this.porcentajeRecomendado = porcentajeRecomendado;
        this.activo = activo;
    }

    public Integer getIdAhorro() {
        return idAhorro;
    }

    public void setIdAhorro(Integer idAhorro) {
        this.idAhorro = idAhorro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    public BigDecimal getAhorroActual() {
        return ahorroActual;
    }

    public void setAhorroActual(BigDecimal ahorroActual) {
        this.ahorroActual = ahorroActual;
    }

    public Integer getPorcentajeRecomendado() {
        return porcentajeRecomendado;
    }

    public void setPorcentajeRecomendado(Integer porcentajeRecomendado) {
        this.porcentajeRecomendado = porcentajeRecomendado;
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
        if (!(o instanceof Ahorro ahorro)) {
            return false;
        }
        return Objects.equals(idAhorro, ahorro.idAhorro)
                && Objects.equals(nombre, ahorro.nombre)
                && Objects.equals(descripcion, ahorro.descripcion)
                && Objects.equals(meta, ahorro.meta)
                && Objects.equals(ahorroActual, ahorro.ahorroActual)
                && Objects.equals(porcentajeRecomendado, ahorro.porcentajeRecomendado)
                && Objects.equals(activo, ahorro.activo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAhorro, nombre, descripcion, meta, ahorroActual, porcentajeRecomendado, activo);
    }

    @Override
    public String toString() {
        return "Ahorro{"
                + "idAhorro=" + idAhorro
                + ", nombre='" + nombre + '\''
                + ", descripcion='" + descripcion + '\''
                + ", meta=" + meta
                + ", ahorroActual=" + ahorroActual
                + ", porcentajeRecomendado=" + porcentajeRecomendado
                + ", activo=" + activo
                + '}';
    }
}
