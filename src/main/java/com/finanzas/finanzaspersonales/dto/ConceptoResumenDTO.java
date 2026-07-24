package com.finanzas.finanzaspersonales.dto;

import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;

public class ConceptoResumenDTO {

    private Integer idConcepto;
    private String nombre;
    private TipoConcepto tipo;
    private Boolean activo;
    private Long cantidadTransacciones;
    private Long cantidadGastosFijos;
    private Boolean puedeCambiarTipo;

    public ConceptoResumenDTO() {
    }

    public ConceptoResumenDTO(
            Integer idConcepto,
            String nombre,
            TipoConcepto tipo,
            Boolean activo,
            Long cantidadTransacciones,
            Long cantidadGastosFijos,
            Boolean puedeCambiarTipo) {
        this.idConcepto = idConcepto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = activo;
        this.cantidadTransacciones = cantidadTransacciones;
        this.cantidadGastosFijos = cantidadGastosFijos;
        this.puedeCambiarTipo = puedeCambiarTipo;
    }

    public Integer getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(Integer idConcepto) {
        this.idConcepto = idConcepto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoConcepto getTipo() {
        return tipo;
    }

    public void setTipo(TipoConcepto tipo) {
        this.tipo = tipo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Long getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public void setCantidadTransacciones(Long cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }

    public Long getCantidadGastosFijos() {
        return cantidadGastosFijos;
    }

    public void setCantidadGastosFijos(Long cantidadGastosFijos) {
        this.cantidadGastosFijos = cantidadGastosFijos;
    }

    public Boolean getPuedeCambiarTipo() {
        return puedeCambiarTipo;
    }

    public void setPuedeCambiarTipo(Boolean puedeCambiarTipo) {
        this.puedeCambiarTipo = puedeCambiarTipo;
    }
}
