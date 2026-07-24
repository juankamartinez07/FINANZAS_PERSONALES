package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ComparacionMensualDTO {

    private final BigDecimal ingresosMesActual;
    private final BigDecimal ingresosMesAnterior;
    private final BigDecimal diferenciaIngresos;
    private final BigDecimal porcentajeIngresos;
    private final BigDecimal gastosMesActual;
    private final BigDecimal gastosMesAnterior;
    private final BigDecimal diferenciaGastos;
    private final BigDecimal porcentajeGastos;
    private final BigDecimal balanceMesActual;
    private final BigDecimal balanceMesAnterior;
    private final BigDecimal diferenciaBalance;
    private final BigDecimal porcentajeBalance;

    public ComparacionMensualDTO(
            BigDecimal ingresosMesActual,
            BigDecimal ingresosMesAnterior,
            BigDecimal diferenciaIngresos,
            BigDecimal porcentajeIngresos,
            BigDecimal gastosMesActual,
            BigDecimal gastosMesAnterior,
            BigDecimal diferenciaGastos,
            BigDecimal porcentajeGastos,
            BigDecimal balanceMesActual,
            BigDecimal balanceMesAnterior,
            BigDecimal diferenciaBalance,
            BigDecimal porcentajeBalance) {
        this.ingresosMesActual = ingresosMesActual;
        this.ingresosMesAnterior = ingresosMesAnterior;
        this.diferenciaIngresos = diferenciaIngresos;
        this.porcentajeIngresos = porcentajeIngresos;
        this.gastosMesActual = gastosMesActual;
        this.gastosMesAnterior = gastosMesAnterior;
        this.diferenciaGastos = diferenciaGastos;
        this.porcentajeGastos = porcentajeGastos;
        this.balanceMesActual = balanceMesActual;
        this.balanceMesAnterior = balanceMesAnterior;
        this.diferenciaBalance = diferenciaBalance;
        this.porcentajeBalance = porcentajeBalance;
    }

    public BigDecimal getIngresosMesActual() { return ingresosMesActual; }

    public BigDecimal getIngresosMesAnterior() { return ingresosMesAnterior; }

    public BigDecimal getDiferenciaIngresos() { return diferenciaIngresos; }

    public BigDecimal getPorcentajeIngresos() { return porcentajeIngresos; }

    public BigDecimal getGastosMesActual() { return gastosMesActual; }

    public BigDecimal getGastosMesAnterior() { return gastosMesAnterior; }

    public BigDecimal getDiferenciaGastos() { return diferenciaGastos; }

    public BigDecimal getPorcentajeGastos() { return porcentajeGastos; }

    public BigDecimal getBalanceMesActual() { return balanceMesActual; }

    public BigDecimal getBalanceMesAnterior() { return balanceMesAnterior; }

    public BigDecimal getDiferenciaBalance() { return diferenciaBalance; }

    public BigDecimal getPorcentajeBalance() { return porcentajeBalance; }
}
