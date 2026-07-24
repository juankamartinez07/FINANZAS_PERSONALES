package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ResumenPeriodoDTO {

    private final BigDecimal totalIngresos;
    private final BigDecimal totalGastos;
    private final BigDecimal balance;
    private final BigDecimal promedioIngresos;
    private final BigDecimal promedioGastos;
    private final Long cantidadTransacciones;
    private final BigDecimal mayorIngreso;
    private final BigDecimal mayorGasto;

    public ResumenPeriodoDTO(
            BigDecimal totalIngresos,
            BigDecimal totalGastos,
            BigDecimal balance,
            BigDecimal promedioIngresos,
            BigDecimal promedioGastos,
            Long cantidadTransacciones,
            BigDecimal mayorIngreso,
            BigDecimal mayorGasto) {
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.balance = balance;
        this.promedioIngresos = promedioIngresos;
        this.promedioGastos = promedioGastos;
        this.cantidadTransacciones = cantidadTransacciones;
        this.mayorIngreso = mayorIngreso;
        this.mayorGasto = mayorGasto;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getPromedioIngresos() {
        return promedioIngresos;
    }

    public BigDecimal getPromedioGastos() {
        return promedioGastos;
    }

    public Long getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public BigDecimal getMayorIngreso() {
        return mayorIngreso;
    }

    public BigDecimal getMayorGasto() {
        return mayorGasto;
    }
}
