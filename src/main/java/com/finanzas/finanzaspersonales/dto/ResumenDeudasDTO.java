package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ResumenDeudasDTO {

    private BigDecimal totalSaldoPendienteActivo;
    private BigDecimal totalPagado;
    private Long cantidadDeudasActivas;
    private Long cantidadDeudasVencidas;
    private BigDecimal totalCuotasMinimas;

    public ResumenDeudasDTO() {
    }

    public ResumenDeudasDTO(
            BigDecimal totalSaldoPendienteActivo,
            BigDecimal totalPagado,
            Long cantidadDeudasActivas,
            Long cantidadDeudasVencidas,
            BigDecimal totalCuotasMinimas) {
        this.totalSaldoPendienteActivo = totalSaldoPendienteActivo;
        this.totalPagado = totalPagado;
        this.cantidadDeudasActivas = cantidadDeudasActivas;
        this.cantidadDeudasVencidas = cantidadDeudasVencidas;
        this.totalCuotasMinimas = totalCuotasMinimas;
    }

    public BigDecimal getTotalSaldoPendienteActivo() {
        return totalSaldoPendienteActivo;
    }

    public void setTotalSaldoPendienteActivo(BigDecimal totalSaldoPendienteActivo) {
        this.totalSaldoPendienteActivo = totalSaldoPendienteActivo;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public Long getCantidadDeudasActivas() {
        return cantidadDeudasActivas;
    }

    public void setCantidadDeudasActivas(Long cantidadDeudasActivas) {
        this.cantidadDeudasActivas = cantidadDeudasActivas;
    }

    public Long getCantidadDeudasVencidas() {
        return cantidadDeudasVencidas;
    }

    public void setCantidadDeudasVencidas(Long cantidadDeudasVencidas) {
        this.cantidadDeudasVencidas = cantidadDeudasVencidas;
    }

    public BigDecimal getTotalCuotasMinimas() {
        return totalCuotasMinimas;
    }

    public void setTotalCuotasMinimas(BigDecimal totalCuotasMinimas) {
        this.totalCuotasMinimas = totalCuotasMinimas;
    }
}
