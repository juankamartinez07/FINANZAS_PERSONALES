package com.finanzas.finanzaspersonales.dto;

public class ResumenConceptosDTO {

    private Long cantidadTotal;
    private Long cantidadActivos;
    private Long cantidadIngresos;
    private Long cantidadGastos;
    private Long cantidadInactivos;

    public ResumenConceptosDTO() {
    }

    public ResumenConceptosDTO(
            Long cantidadTotal,
            Long cantidadActivos,
            Long cantidadIngresos,
            Long cantidadGastos,
            Long cantidadInactivos) {
        this.cantidadTotal = cantidadTotal;
        this.cantidadActivos = cantidadActivos;
        this.cantidadIngresos = cantidadIngresos;
        this.cantidadGastos = cantidadGastos;
        this.cantidadInactivos = cantidadInactivos;
    }

    public Long getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Long cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public Long getCantidadActivos() {
        return cantidadActivos;
    }

    public void setCantidadActivos(Long cantidadActivos) {
        this.cantidadActivos = cantidadActivos;
    }

    public Long getCantidadIngresos() {
        return cantidadIngresos;
    }

    public void setCantidadIngresos(Long cantidadIngresos) {
        this.cantidadIngresos = cantidadIngresos;
    }

    public Long getCantidadGastos() {
        return cantidadGastos;
    }

    public void setCantidadGastos(Long cantidadGastos) {
        this.cantidadGastos = cantidadGastos;
    }

    public Long getCantidadInactivos() {
        return cantidadInactivos;
    }

    public void setCantidadInactivos(Long cantidadInactivos) {
        this.cantidadInactivos = cantidadInactivos;
    }
}
