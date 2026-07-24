package com.finanzas.finanzaspersonales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTransaccion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(length = 255)
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_concepto", nullable = false)
    private Concepto concepto;

    public Transaccion() {
    }

    public Transaccion(
            Integer idTransaccion,
            LocalDate fecha,
            BigDecimal valor,
            String observacion,
            LocalDateTime fechaRegistro,
            Cuenta cuenta,
            Concepto concepto) {
        this.idTransaccion = idTransaccion;
        this.fecha = fecha;
        this.valor = valor;
        this.observacion = observacion;
        this.fechaRegistro = fechaRegistro;
        this.cuenta = cuenta;
        this.concepto = concepto;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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
        if (!(o instanceof Transaccion transaccion)) {
            return false;
        }
        return Objects.equals(idTransaccion, transaccion.idTransaccion)
                && Objects.equals(fecha, transaccion.fecha)
                && Objects.equals(valor, transaccion.valor)
                && Objects.equals(observacion, transaccion.observacion)
                && Objects.equals(fechaRegistro, transaccion.fechaRegistro)
                && Objects.equals(cuenta, transaccion.cuenta)
                && Objects.equals(concepto, transaccion.concepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaccion, fecha, valor, observacion, fechaRegistro, cuenta, concepto);
    }

    @Override
    public String toString() {
        return "Transaccion{"
                + "idTransaccion=" + idTransaccion
                + ", fecha=" + fecha
                + ", valor=" + valor
                + ", observacion='" + observacion + '\''
                + ", fechaRegistro=" + fechaRegistro
                + ", cuenta=" + cuenta
                + ", concepto=" + concepto
                + '}';
    }
}
