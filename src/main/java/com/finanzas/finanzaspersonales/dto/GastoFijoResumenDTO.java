package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GastoFijoResumenDTO {

    private Integer idGastoFijo;
    private String nombre;
    private BigDecimal valor;
    private Integer diaPago;
    private LocalDate fechaInicio;
    private Boolean activo;
    private String nombreCuenta;
    private String nombreConcepto;
    private LocalDate proximaFechaPago;
    private Integer diasParaPago;
    private String estadoVisual;

    public GastoFijoResumenDTO() {
    }

    public GastoFijoResumenDTO(
            Integer idGastoFijo,
            String nombre,
            BigDecimal valor,
            Integer diaPago,
            LocalDate fechaInicio,
            Boolean activo,
            String nombreCuenta,
            String nombreConcepto,
            LocalDate proximaFechaPago,
            Integer diasParaPago,
            String estadoVisual) {
        this.idGastoFijo = idGastoFijo;
        this.nombre = nombre;
        this.valor = valor;
        this.diaPago = diaPago;
        this.fechaInicio = fechaInicio;
        this.activo = activo;
        this.nombreCuenta = nombreCuenta;
        this.nombreConcepto = nombreConcepto;
        this.proximaFechaPago = proximaFechaPago;
        this.diasParaPago = diasParaPago;
        this.estadoVisual = estadoVisual;
    }

    public Integer getIdGastoFijo() {
        return idGastoFijo;
    }

    public void setIdGastoFijo(Integer idGastoFijo) {
        this.idGastoFijo = idGastoFijo;
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

    public Integer getDiaPago() {
        return diaPago;
    }

    public void setDiaPago(Integer diaPago) {
        this.diaPago = diaPago;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getNombreConcepto() {
        return nombreConcepto;
    }

    public void setNombreConcepto(String nombreConcepto) {
        this.nombreConcepto = nombreConcepto;
    }

    public LocalDate getProximaFechaPago() {
        return proximaFechaPago;
    }

    public void setProximaFechaPago(LocalDate proximaFechaPago) {
        this.proximaFechaPago = proximaFechaPago;
    }

    public Integer getDiasParaPago() {
        return diasParaPago;
    }

    public void setDiasParaPago(Integer diasParaPago) {
        this.diasParaPago = diasParaPago;
    }

    public String getEstadoVisual() {
        return estadoVisual;
    }

    public void setEstadoVisual(String estadoVisual) {
        this.estadoVisual = estadoVisual;
    }
}
