package com.finanzas.finanzaspersonales.dto;

import com.finanzas.finanzaspersonales.entity.Transaccion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CuentaDetalleDTO {

    private CuentaResumenDTO cuenta;
    private BigDecimal totalIngresos = BigDecimal.ZERO;
    private BigDecimal totalGastos = BigDecimal.ZERO;
    private List<Transaccion> ultimasTransacciones = new ArrayList<>();

    public CuentaDetalleDTO() {
    }

    public CuentaDetalleDTO(
            CuentaResumenDTO cuenta,
            BigDecimal totalIngresos,
            BigDecimal totalGastos,
            List<Transaccion> ultimasTransacciones) {
        this.cuenta = cuenta;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.ultimasTransacciones = ultimasTransacciones;
    }

    public CuentaResumenDTO getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaResumenDTO cuenta) {
        this.cuenta = cuenta;
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

    public List<Transaccion> getUltimasTransacciones() {
        return ultimasTransacciones;
    }

    public void setUltimasTransacciones(List<Transaccion> ultimasTransacciones) {
        this.ultimasTransacciones = ultimasTransacciones;
    }
}
