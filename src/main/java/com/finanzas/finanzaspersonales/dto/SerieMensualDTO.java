package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class SerieMensualDTO {

    private final String periodo;
    private final BigDecimal ingresos;
    private final BigDecimal gastos;
    private final BigDecimal balance;

    public SerieMensualDTO(String periodo, BigDecimal ingresos, BigDecimal gastos, BigDecimal balance) {
        this.periodo = periodo;
        this.ingresos = ingresos;
        this.gastos = gastos;
        this.balance = balance;
    }

    public String getPeriodo() {
        return periodo;
    }

    public BigDecimal getIngresos() {
        return ingresos;
    }

    public BigDecimal getGastos() {
        return gastos;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
