package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class SaldoCuentaDTO {

    private Integer idCuenta;
    private String nombreCuenta;
    private BigDecimal saldo = BigDecimal.ZERO;

    public SaldoCuentaDTO() {
    }

    public SaldoCuentaDTO(Integer idCuenta, String nombreCuenta, BigDecimal saldo) {
        this.idCuenta = idCuenta;
        this.nombreCuenta = nombreCuenta;
        this.saldo = saldo;
    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
