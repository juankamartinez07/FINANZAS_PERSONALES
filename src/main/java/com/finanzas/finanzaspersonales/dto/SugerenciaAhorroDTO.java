package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class SugerenciaAhorroDTO {

    private BigDecimal valorSugerido = BigDecimal.ZERO;
    private BigDecimal porcentajeSugerido = BigDecimal.ZERO;
    private String explicacion;

    public SugerenciaAhorroDTO() {
    }

    public SugerenciaAhorroDTO(BigDecimal valorSugerido, BigDecimal porcentajeSugerido, String explicacion) {
        this.valorSugerido = valorSugerido;
        this.porcentajeSugerido = porcentajeSugerido;
        this.explicacion = explicacion;
    }

    public BigDecimal getValorSugerido() {
        return valorSugerido;
    }

    public void setValorSugerido(BigDecimal valorSugerido) {
        this.valorSugerido = valorSugerido;
    }

    public BigDecimal getPorcentajeSugerido() {
        return porcentajeSugerido;
    }

    public void setPorcentajeSugerido(BigDecimal porcentajeSugerido) {
        this.porcentajeSugerido = porcentajeSugerido;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }
}
