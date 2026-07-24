package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DeudaResumenDTO {

    private Integer idDeuda;
    private String nombre;
    private String entidad;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private BigDecimal cuotaMinima;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private Boolean activo;
    private String nombreCuenta;
    private BigDecimal porcentajePagado;
    private BigDecimal valorPagado;
    private String estadoVisual;
    private Integer diasParaVencimiento;

    public DeudaResumenDTO() {
    }

    public DeudaResumenDTO(
            Integer idDeuda,
            String nombre,
            String entidad,
            BigDecimal saldoInicial,
            BigDecimal saldoActual,
            BigDecimal cuotaMinima,
            LocalDate fechaInicio,
            LocalDate fechaVencimiento,
            Boolean activo,
            String nombreCuenta,
            BigDecimal porcentajePagado,
            BigDecimal valorPagado,
            String estadoVisual,
            Integer diasParaVencimiento) {
        this.idDeuda = idDeuda;
        this.nombre = nombre;
        this.entidad = entidad;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoActual;
        this.cuotaMinima = cuotaMinima;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.activo = activo;
        this.nombreCuenta = nombreCuenta;
        this.porcentajePagado = porcentajePagado;
        this.valorPagado = valorPagado;
        this.estadoVisual = estadoVisual;
        this.diasParaVencimiento = diasParaVencimiento;
    }

    public Integer getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(Integer idDeuda) {
        this.idDeuda = idDeuda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public BigDecimal getCuotaMinima() {
        return cuotaMinima;
    }

    public void setCuotaMinima(BigDecimal cuotaMinima) {
        this.cuotaMinima = cuotaMinima;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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

    public BigDecimal getPorcentajePagado() {
        return porcentajePagado;
    }

    public void setPorcentajePagado(BigDecimal porcentajePagado) {
        this.porcentajePagado = porcentajePagado;
    }

    public BigDecimal getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(BigDecimal valorPagado) {
        this.valorPagado = valorPagado;
    }

    public String getEstadoVisual() {
        return estadoVisual;
    }

    public void setEstadoVisual(String estadoVisual) {
        this.estadoVisual = estadoVisual;
    }

    public Integer getDiasParaVencimiento() {
        return diasParaVencimiento;
    }

    public void setDiasParaVencimiento(Integer diasParaVencimiento) {
        this.diasParaVencimiento = diasParaVencimiento;
    }
}
