package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ResumenAhorrosDTO {

    private BigDecimal totalAhorradoActivo;
    private BigDecimal totalObjetivosActivos;
    private BigDecimal valorTotalFaltante;
    private Long cantidadMetasActivas;
    private Long cantidadMetasCompletadas;
    private BigDecimal porcentajeGeneralCumplimiento;

    public ResumenAhorrosDTO() {
    }

    public ResumenAhorrosDTO(
            BigDecimal totalAhorradoActivo,
            BigDecimal totalObjetivosActivos,
            BigDecimal valorTotalFaltante,
            Long cantidadMetasActivas,
            Long cantidadMetasCompletadas,
            BigDecimal porcentajeGeneralCumplimiento) {
        this.totalAhorradoActivo = totalAhorradoActivo;
        this.totalObjetivosActivos = totalObjetivosActivos;
        this.valorTotalFaltante = valorTotalFaltante;
        this.cantidadMetasActivas = cantidadMetasActivas;
        this.cantidadMetasCompletadas = cantidadMetasCompletadas;
        this.porcentajeGeneralCumplimiento = porcentajeGeneralCumplimiento;
    }

    public BigDecimal getTotalAhorradoActivo() {
        return totalAhorradoActivo;
    }

    public void setTotalAhorradoActivo(BigDecimal totalAhorradoActivo) {
        this.totalAhorradoActivo = totalAhorradoActivo;
    }

    public BigDecimal getTotalObjetivosActivos() {
        return totalObjetivosActivos;
    }

    public void setTotalObjetivosActivos(BigDecimal totalObjetivosActivos) {
        this.totalObjetivosActivos = totalObjetivosActivos;
    }

    public BigDecimal getValorTotalFaltante() {
        return valorTotalFaltante;
    }

    public void setValorTotalFaltante(BigDecimal valorTotalFaltante) {
        this.valorTotalFaltante = valorTotalFaltante;
    }

    public Long getCantidadMetasActivas() {
        return cantidadMetasActivas;
    }

    public void setCantidadMetasActivas(Long cantidadMetasActivas) {
        this.cantidadMetasActivas = cantidadMetasActivas;
    }

    public Long getCantidadMetasCompletadas() {
        return cantidadMetasCompletadas;
    }

    public void setCantidadMetasCompletadas(Long cantidadMetasCompletadas) {
        this.cantidadMetasCompletadas = cantidadMetasCompletadas;
    }

    public BigDecimal getPorcentajeGeneralCumplimiento() {
        return porcentajeGeneralCumplimiento;
    }

    public void setPorcentajeGeneralCumplimiento(BigDecimal porcentajeGeneralCumplimiento) {
        this.porcentajeGeneralCumplimiento = porcentajeGeneralCumplimiento;
    }
}
