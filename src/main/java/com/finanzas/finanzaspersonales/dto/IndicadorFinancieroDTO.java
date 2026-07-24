package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class IndicadorFinancieroDTO {

    private String nombre;
    private BigDecimal valor;
    private String descripcion;
    private String icono;
    private String estadoVisual;
    private boolean porcentaje;

    public IndicadorFinancieroDTO() {
    }

    public IndicadorFinancieroDTO(
            String nombre,
            BigDecimal valor,
            String descripcion,
            String icono,
            String estadoVisual,
            boolean porcentaje) {
        this.nombre = nombre;
        this.valor = valor;
        this.descripcion = descripcion;
        this.icono = icono;
        this.estadoVisual = estadoVisual;
        this.porcentaje = porcentaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getEstadoVisual() {
        return estadoVisual;
    }

    public void setEstadoVisual(String estadoVisual) {
        this.estadoVisual = estadoVisual;
    }

    public boolean isPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(boolean porcentaje) {
        this.porcentaje = porcentaje;
    }
}
