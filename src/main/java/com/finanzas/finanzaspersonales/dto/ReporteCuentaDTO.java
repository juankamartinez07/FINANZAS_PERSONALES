package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class ReporteCuentaDTO {

    private final Integer idCuenta;
    private final String nombreCuenta;
    private final BigDecimal totalIngresos;
    private final BigDecimal totalGastos;
    private final BigDecimal balance;
    private final Long cantidadTransacciones;
    private final BigDecimal participacionMovimientos;

    public ReporteCuentaDTO(
            Integer idCuenta,
            String nombreCuenta,
            BigDecimal totalIngresos,
            BigDecimal totalGastos,
            BigDecimal balance,
            Long cantidadTransacciones,
            BigDecimal participacionMovimientos) {
        this.idCuenta = idCuenta;
        this.nombreCuenta = nombreCuenta;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.balance = balance;
        this.cantidadTransacciones = cantidadTransacciones;
        this.participacionMovimientos = participacionMovimientos;
    }

    public Integer getIdCuenta() { return idCuenta; }

    public String getNombreCuenta() { return nombreCuenta; }

    public BigDecimal getTotalIngresos() { return totalIngresos; }

    public BigDecimal getTotalGastos() { return totalGastos; }

    public BigDecimal getBalance() { return balance; }

    public Long getCantidadTransacciones() { return cantidadTransacciones; }

    public BigDecimal getParticipacionMovimientos() { return participacionMovimientos; }
}
