package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class CuentaResumenDTO {

    private Integer idCuenta;
    private String nombre;
    private Boolean activo;
    private BigDecimal saldoCalculado = BigDecimal.ZERO;
    private Long cantidadTransacciones = 0L;

    public CuentaResumenDTO() {
    }

    public CuentaResumenDTO(
            Integer idCuenta,
            String nombre,
            Boolean activo,
            BigDecimal saldoCalculado,
            Long cantidadTransacciones) {
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.activo = activo;
        this.saldoCalculado = saldoCalculado;
        this.cantidadTransacciones = cantidadTransacciones;
    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getSaldoCalculado() {
        return saldoCalculado;
    }

    public void setSaldoCalculado(BigDecimal saldoCalculado) {
        this.saldoCalculado = saldoCalculado;
    }

    public Long getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public void setCantidadTransacciones(Long cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }
}
