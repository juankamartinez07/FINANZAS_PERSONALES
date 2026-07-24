package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ResumenCuentasDTO {

    private BigDecimal saldoTotal = BigDecimal.ZERO;
    private Long cantidadActivas = 0L;
    private Long cantidadTotal = 0L;
    private String cuentaMayorSaldo;

    public ResumenCuentasDTO() {
    }

    public ResumenCuentasDTO(BigDecimal saldoTotal, Long cantidadActivas, Long cantidadTotal, String cuentaMayorSaldo) {
        this.saldoTotal = saldoTotal;
        this.cantidadActivas = cantidadActivas;
        this.cantidadTotal = cantidadTotal;
        this.cuentaMayorSaldo = cuentaMayorSaldo;
    }

    public BigDecimal getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(BigDecimal saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public Long getCantidadActivas() {
        return cantidadActivas;
    }

    public void setCantidadActivas(Long cantidadActivas) {
        this.cantidadActivas = cantidadActivas;
    }

    public Long getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Long cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public String getCuentaMayorSaldo() {
        return cuentaMayorSaldo;
    }

    public void setCuentaMayorSaldo(String cuentaMayorSaldo) {
        this.cuentaMayorSaldo = cuentaMayorSaldo;
    }
}
