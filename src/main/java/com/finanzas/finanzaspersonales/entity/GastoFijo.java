package com.finanzas.finanzaspersonales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "gastos_fijos")
public class GastoFijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto")
    private Integer idGastoFijo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private Integer diaPago;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_concepto", nullable = false)
    private Concepto concepto;

    public GastoFijo() {
    }

    public GastoFijo(
            Integer idGastoFijo,
            String nombre,
            BigDecimal valor,
            Integer diaPago,
            Boolean activo,
            Cuenta cuenta,
            Concepto concepto) {
        this.idGastoFijo = idGastoFijo;
        this.nombre = nombre;
        this.valor = valor;
        this.diaPago = diaPago;
        this.activo = activo;
        this.cuenta = cuenta;
        this.concepto = concepto;
    }

    public Integer getIdGastoFijo() {
        return idGastoFijo;
    }

    public void setIdGastoFijo(Integer idGastoFijo) {
        this.idGastoFijo = idGastoFijo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getDiaPago() {
        return diaPago;
    }

    public void setDiaPago(Integer diaPago) {
        this.diaPago = diaPago;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GastoFijo gastoFijo)) {
            return false;
        }
        return Objects.equals(idGastoFijo, gastoFijo.idGastoFijo)
                && Objects.equals(nombre, gastoFijo.nombre)
                && Objects.equals(valor, gastoFijo.valor)
                && Objects.equals(diaPago, gastoFijo.diaPago)
                && Objects.equals(activo, gastoFijo.activo)
                && Objects.equals(cuenta, gastoFijo.cuenta)
                && Objects.equals(concepto, gastoFijo.concepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGastoFijo, nombre, valor, diaPago, activo, cuenta, concepto);
    }

    @Override
    public String toString() {
        return "GastoFijo{"
                + "idGastoFijo=" + idGastoFijo
                + ", nombre='" + nombre + '\''
                + ", valor=" + valor
                + ", diaPago=" + diaPago
                + ", activo=" + activo
                + ", cuenta=" + cuenta
                + ", concepto=" + concepto
                + '}';
    }
}
