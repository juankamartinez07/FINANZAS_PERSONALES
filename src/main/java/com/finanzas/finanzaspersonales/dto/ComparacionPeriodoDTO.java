package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ComparacionPeriodoDTO {

    private BigDecimal ingresosActuales = BigDecimal.ZERO;
    private BigDecimal ingresosAnteriores = BigDecimal.ZERO;
    private BigDecimal diferenciaIngresos = BigDecimal.ZERO;
    private BigDecimal porcentajeIngresos;
    private BigDecimal gastosActuales = BigDecimal.ZERO;
    private BigDecimal gastosAnteriores = BigDecimal.ZERO;
    private BigDecimal diferenciaGastos = BigDecimal.ZERO;
    private BigDecimal porcentajeGastos;
    private BigDecimal balanceActual = BigDecimal.ZERO;
    private BigDecimal balanceAnterior = BigDecimal.ZERO;
    private BigDecimal diferenciaBalance = BigDecimal.ZERO;
    private BigDecimal porcentajeBalance;
    private boolean comparable;
    private String mensaje;

    public ComparacionPeriodoDTO() {
    }

    public ComparacionPeriodoDTO(
            BigDecimal ingresosActuales,
            BigDecimal ingresosAnteriores,
            BigDecimal diferenciaIngresos,
            BigDecimal porcentajeIngresos,
            BigDecimal gastosActuales,
            BigDecimal gastosAnteriores,
            BigDecimal diferenciaGastos,
            BigDecimal porcentajeGastos,
            BigDecimal balanceActual,
            BigDecimal balanceAnterior,
            BigDecimal diferenciaBalance,
            BigDecimal porcentajeBalance,
            boolean comparable,
            String mensaje) {
        this.ingresosActuales = ingresosActuales;
        this.ingresosAnteriores = ingresosAnteriores;
        this.diferenciaIngresos = diferenciaIngresos;
        this.porcentajeIngresos = porcentajeIngresos;
        this.gastosActuales = gastosActuales;
        this.gastosAnteriores = gastosAnteriores;
        this.diferenciaGastos = diferenciaGastos;
        this.porcentajeGastos = porcentajeGastos;
        this.balanceActual = balanceActual;
        this.balanceAnterior = balanceAnterior;
        this.diferenciaBalance = diferenciaBalance;
        this.porcentajeBalance = porcentajeBalance;
        this.comparable = comparable;
        this.mensaje = mensaje;
    }

    public BigDecimal getIngresosActuales() {
        return ingresosActuales;
    }

    public void setIngresosActuales(BigDecimal ingresosActuales) {
        this.ingresosActuales = ingresosActuales;
    }

    public BigDecimal getIngresosAnteriores() {
        return ingresosAnteriores;
    }

    public void setIngresosAnteriores(BigDecimal ingresosAnteriores) {
        this.ingresosAnteriores = ingresosAnteriores;
    }

    public BigDecimal getDiferenciaIngresos() {
        return diferenciaIngresos;
    }

    public void setDiferenciaIngresos(BigDecimal diferenciaIngresos) {
        this.diferenciaIngresos = diferenciaIngresos;
    }

    public BigDecimal getPorcentajeIngresos() {
        return porcentajeIngresos;
    }

    public void setPorcentajeIngresos(BigDecimal porcentajeIngresos) {
        this.porcentajeIngresos = porcentajeIngresos;
    }

    public BigDecimal getGastosActuales() {
        return gastosActuales;
    }

    public void setGastosActuales(BigDecimal gastosActuales) {
        this.gastosActuales = gastosActuales;
    }

    public BigDecimal getGastosAnteriores() {
        return gastosAnteriores;
    }

    public void setGastosAnteriores(BigDecimal gastosAnteriores) {
        this.gastosAnteriores = gastosAnteriores;
    }

    public BigDecimal getDiferenciaGastos() {
        return diferenciaGastos;
    }

    public void setDiferenciaGastos(BigDecimal diferenciaGastos) {
        this.diferenciaGastos = diferenciaGastos;
    }

    public BigDecimal getPorcentajeGastos() {
        return porcentajeGastos;
    }

    public void setPorcentajeGastos(BigDecimal porcentajeGastos) {
        this.porcentajeGastos = porcentajeGastos;
    }

    public BigDecimal getBalanceActual() {
        return balanceActual;
    }

    public void setBalanceActual(BigDecimal balanceActual) {
        this.balanceActual = balanceActual;
    }

    public BigDecimal getBalanceAnterior() {
        return balanceAnterior;
    }

    public void setBalanceAnterior(BigDecimal balanceAnterior) {
        this.balanceAnterior = balanceAnterior;
    }

    public BigDecimal getDiferenciaBalance() {
        return diferenciaBalance;
    }

    public void setDiferenciaBalance(BigDecimal diferenciaBalance) {
        this.diferenciaBalance = diferenciaBalance;
    }

    public BigDecimal getPorcentajeBalance() {
        return porcentajeBalance;
    }

    public void setPorcentajeBalance(BigDecimal porcentajeBalance) {
        this.porcentajeBalance = porcentajeBalance;
    }

    public boolean isComparable() {
        return comparable;
    }

    public void setComparable(boolean comparable) {
        this.comparable = comparable;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
