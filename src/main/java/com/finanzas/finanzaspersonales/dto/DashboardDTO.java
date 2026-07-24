package com.finanzas.finanzaspersonales.dto;

import com.finanzas.finanzaspersonales.entity.Transaccion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashboardDTO {

    private BigDecimal saldoGeneral = BigDecimal.ZERO;
    private BigDecimal ingresosMes = BigDecimal.ZERO;
    private BigDecimal gastosMes = BigDecimal.ZERO;
    private BigDecimal balanceMes = BigDecimal.ZERO;
    private BigDecimal totalDeudas = BigDecimal.ZERO;
    private BigDecimal totalAhorros = BigDecimal.ZERO;
    private Long cantidadTransacciones = 0L;
    private Long cantidadDeudas = 0L;
    private Long cantidadMetasAhorro = 0L;
    private List<Transaccion> ultimasTransacciones = new ArrayList<>();

    public DashboardDTO() {
    }

    public DashboardDTO(
            BigDecimal saldoGeneral,
            BigDecimal ingresosMes,
            BigDecimal gastosMes,
            BigDecimal balanceMes,
            BigDecimal totalDeudas,
            BigDecimal totalAhorros,
            Long cantidadTransacciones,
            Long cantidadDeudas,
            Long cantidadMetasAhorro,
            List<Transaccion> ultimasTransacciones) {
        this.saldoGeneral = saldoGeneral;
        this.ingresosMes = ingresosMes;
        this.gastosMes = gastosMes;
        this.balanceMes = balanceMes;
        this.totalDeudas = totalDeudas;
        this.totalAhorros = totalAhorros;
        this.cantidadTransacciones = cantidadTransacciones;
        this.cantidadDeudas = cantidadDeudas;
        this.cantidadMetasAhorro = cantidadMetasAhorro;
        this.ultimasTransacciones = ultimasTransacciones;
    }

    public BigDecimal getSaldoGeneral() {
        return saldoGeneral;
    }

    public void setSaldoGeneral(BigDecimal saldoGeneral) {
        this.saldoGeneral = saldoGeneral;
    }

    public BigDecimal getIngresosMes() {
        return ingresosMes;
    }

    public void setIngresosMes(BigDecimal ingresosMes) {
        this.ingresosMes = ingresosMes;
    }

    public BigDecimal getGastosMes() {
        return gastosMes;
    }

    public void setGastosMes(BigDecimal gastosMes) {
        this.gastosMes = gastosMes;
    }

    public BigDecimal getBalanceMes() {
        return balanceMes;
    }

    public void setBalanceMes(BigDecimal balanceMes) {
        this.balanceMes = balanceMes;
    }

    public BigDecimal getTotalDeudas() {
        return totalDeudas;
    }

    public void setTotalDeudas(BigDecimal totalDeudas) {
        this.totalDeudas = totalDeudas;
    }

    public BigDecimal getTotalAhorros() {
        return totalAhorros;
    }

    public void setTotalAhorros(BigDecimal totalAhorros) {
        this.totalAhorros = totalAhorros;
    }

    public Long getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public void setCantidadTransacciones(Long cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }

    public Long getCantidadDeudas() {
        return cantidadDeudas;
    }

    public void setCantidadDeudas(Long cantidadDeudas) {
        this.cantidadDeudas = cantidadDeudas;
    }

    public Long getCantidadMetasAhorro() {
        return cantidadMetasAhorro;
    }

    public void setCantidadMetasAhorro(Long cantidadMetasAhorro) {
        this.cantidadMetasAhorro = cantidadMetasAhorro;
    }

    public List<Transaccion> getUltimasTransacciones() {
        return ultimasTransacciones;
    }

    public void setUltimasTransacciones(List<Transaccion> ultimasTransacciones) {
        this.ultimasTransacciones = ultimasTransacciones;
    }
}
