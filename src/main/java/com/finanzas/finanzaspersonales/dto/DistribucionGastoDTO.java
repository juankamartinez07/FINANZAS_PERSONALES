package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class DistribucionGastoDTO {

    private Integer idConcepto;
    private String nombreConcepto;
    private BigDecimal totalGastado;
    private BigDecimal porcentaje;
    private Long cantidadTransacciones;
    private BigDecimal diferenciaPeriodoAnterior;

    public DistribucionGastoDTO() {
    }

    public DistribucionGastoDTO(
            Integer idConcepto,
            String nombreConcepto,
            BigDecimal totalGastado,
            BigDecimal porcentaje,
            Long cantidadTransacciones,
            BigDecimal diferenciaPeriodoAnterior) {
        this.idConcepto = idConcepto;
        this.nombreConcepto = nombreConcepto;
        this.totalGastado = totalGastado;
        this.porcentaje = porcentaje;
        this.cantidadTransacciones = cantidadTransacciones;
        this.diferenciaPeriodoAnterior = diferenciaPeriodoAnterior;
    }

    public Integer getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(Integer idConcepto) {
        this.idConcepto = idConcepto;
    }

    public String getNombreConcepto() {
        return nombreConcepto;
    }

    public void setNombreConcepto(String nombreConcepto) {
        this.nombreConcepto = nombreConcepto;
    }

    public BigDecimal getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(BigDecimal totalGastado) {
        this.totalGastado = totalGastado;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Long getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public void setCantidadTransacciones(Long cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }

    public BigDecimal getDiferenciaPeriodoAnterior() {
        return diferenciaPeriodoAnterior;
    }

    public void setDiferenciaPeriodoAnterior(BigDecimal diferenciaPeriodoAnterior) {
        this.diferenciaPeriodoAnterior = diferenciaPeriodoAnterior;
    }
}
