package com.finanzas.finanzaspersonales.entity;

import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "conceptos",
        uniqueConstraints = @UniqueConstraint(name = "uk_conceptos_nombre_tipo", columnNames = {"nombre", "tipo"}))
public class Concepto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConcepto;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConcepto tipo;

    @Column(nullable = false)
    private Boolean activo = true;

    public Concepto() {
    }

    public Concepto(Integer idConcepto, String nombre, TipoConcepto tipo, Boolean activo) {
        this.idConcepto = idConcepto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = activo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Concepto concepto)) {
            return false;
        }
        return Objects.equals(idConcepto, concepto.idConcepto)
                && Objects.equals(nombre, concepto.nombre)
                && tipo == concepto.tipo
                && Objects.equals(activo, concepto.activo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idConcepto, nombre, tipo, activo);
    }

    @Override
    public String toString() {
        return "Concepto{"
                + "idConcepto=" + idConcepto
                + ", nombre='" + nombre + '\''
                + ", tipo=" + tipo
                + ", activo=" + activo
                + '}';
    }
}
