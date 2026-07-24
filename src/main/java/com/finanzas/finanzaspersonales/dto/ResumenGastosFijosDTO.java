package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ResumenGastosFijosDTO {

    private BigDecimal totalMensualComprometido;
    private Long cantidadGastosActivos;
    private Long pagosProximos;
    private BigDecimal valorProximoPagar;
    private String gastoMayorValor;

    public ResumenGastosFijosDTO() {
    }

    public ResumenGastosFijosDTO(
            BigDecimal totalMensualComprometido,
            Long cantidadGastosActivos,
            Long pagosProximos,
            BigDecimal valorProximoPagar,
            String gastoMayorValor) {
        this.totalMensualComprometido = totalMensualComprometido;
        this.cantidadGastosActivos = cantidadGastosActivos;
        this.pagosProximos = pagosProximos;
        this.valorProximoPagar = valorProximoPagar;
        this.gastoMayorValor = gastoMayorValor;
    }

    public BigDecimal getTotalMensualComprometido() {
        return totalMensualComprometido;
    }

    public void setTotalMensualComprometido(BigDecimal totalMensualComprometido) {
        this.totalMensualComprometido = totalMensualComprometido;
    }

    public Long getCantidadGastosActivos() {
        return cantidadGastosActivos;
    }

    public void setCantidadGastosActivos(Long cantidadGastosActivos) {
        this.cantidadGastosActivos = cantidadGastosActivos;
    }

    public Long getPagosProximos() {
        return pagosProximos;
    }

    public void setPagosProximos(Long pagosProximos) {
        this.pagosProximos = pagosProximos;
    }

    public BigDecimal getValorProximoPagar() {
        return valorProximoPagar;
    }

    public void setValorProximoPagar(BigDecimal valorProximoPagar) {
        this.valorProximoPagar = valorProximoPagar;
    }

    public String getGastoMayorValor() {
        return gastoMayorValor;
    }

    public void setGastoMayorValor(String gastoMayorValor) {
        this.gastoMayorValor = gastoMayorValor;
    }
}
