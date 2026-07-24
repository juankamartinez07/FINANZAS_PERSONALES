package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ResumenTransaccionesDTO {

    private BigDecimal totalIngresos = BigDecimal.ZERO;
    private BigDecimal totalGastos = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private Long cantidadMovimientos = 0L;

    public ResumenTransaccionesDTO() {
    }

    public ResumenTransaccionesDTO(
            BigDecimal totalIngresos,
            BigDecimal totalGastos,
            BigDecimal balance,
            Long cantidadMovimientos) {
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.balance = balance;
        this.cantidadMovimientos = cantidadMovimientos;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getCantidadMovimientos() {
        return cantidadMovimientos;
    }

    public void setCantidadMovimientos(Long cantidadMovimientos) {
        this.cantidadMovimientos = cantidadMovimientos;
    }
}
