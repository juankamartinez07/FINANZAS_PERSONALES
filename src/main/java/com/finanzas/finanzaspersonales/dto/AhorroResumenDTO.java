package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AhorroResumenDTO {

    private Integer idAhorro;
    private String nombre;
    private String descripcion;
    private BigDecimal valorObjetivo;
    private BigDecimal valorAhorrado;
    private BigDecimal valorFaltante;
    private BigDecimal porcentajeAvance;
    private LocalDate fechaInicio;
    private LocalDate fechaObjetivo;
    private Boolean activo;
    private String estadoVisual;
    private Integer diasRestantes;

    public AhorroResumenDTO() {
    }

    public AhorroResumenDTO(
            Integer idAhorro,
            String nombre,
            String descripcion,
            BigDecimal valorObjetivo,
            BigDecimal valorAhorrado,
            BigDecimal valorFaltante,
            BigDecimal porcentajeAvance,
            LocalDate fechaInicio,
            LocalDate fechaObjetivo,
            Boolean activo,
            String estadoVisual,
            Integer diasRestantes) {
        this.idAhorro = idAhorro;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valorObjetivo = valorObjetivo;
        this.valorAhorrado = valorAhorrado;
        this.valorFaltante = valorFaltante;
        this.porcentajeAvance = porcentajeAvance;
        this.fechaInicio = fechaInicio;
        this.fechaObjetivo = fechaObjetivo;
        this.activo = activo;
        this.estadoVisual = estadoVisual;
        this.diasRestantes = diasRestantes;
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

    public BigDecimal getValorObjetivo() {
        return valorObjetivo;
    }

    public void setValorObjetivo(BigDecimal valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    public BigDecimal getValorAhorrado() {
        return valorAhorrado;
    }

    public void setValorAhorrado(BigDecimal valorAhorrado) {
        this.valorAhorrado = valorAhorrado;
    }

    public BigDecimal getValorFaltante() {
        return valorFaltante;
    }

    public void setValorFaltante(BigDecimal valorFaltante) {
        this.valorFaltante = valorFaltante;
    }

    public BigDecimal getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(BigDecimal porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaObjetivo() {
        return fechaObjetivo;
    }

    public void setFechaObjetivo(LocalDate fechaObjetivo) {
        this.fechaObjetivo = fechaObjetivo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstadoVisual() {
        return estadoVisual;
    }

    public void setEstadoVisual(String estadoVisual) {
        this.estadoVisual = estadoVisual;
    }

    public Integer getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(Integer diasRestantes) {
        this.diasRestantes = diasRestantes;
    }
}
