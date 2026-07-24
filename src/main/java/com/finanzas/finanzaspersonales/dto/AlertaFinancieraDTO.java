package com.finanzas.finanzaspersonales.dto;

public class AlertaFinancieraDTO {

    private String codigo;
    private String titulo;
    private String descripcion;
    private String nivel;
    private String icono;
    private String ruta;

    public AlertaFinancieraDTO() {
    }

    public AlertaFinancieraDTO(String codigo, String titulo, String descripcion, String nivel, String icono, String ruta) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.icono = icono;
        this.ruta = ruta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
