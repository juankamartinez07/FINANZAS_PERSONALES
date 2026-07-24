package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;

public class DistribucionDTO {

    private final Integer id;
    private final String nombre;
    private final BigDecimal total;
    private final BigDecimal porcentaje;
    private final Long cantidad;

    public DistribucionDTO(Integer id, String nombre, BigDecimal total, BigDecimal porcentaje, Long cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.total = total;
        this.porcentaje = porcentaje;
        this.cantidad = cantidad;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public Long getCantidad() {
        return cantidad;
    }
}
