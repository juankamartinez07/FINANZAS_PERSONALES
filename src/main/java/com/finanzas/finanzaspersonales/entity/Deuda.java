package com.finanzas.finanzaspersonales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "deudas")
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDeuda;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String entidad;

    @Column(nullable = false)
    private BigDecimal saldoInicial;

    @Column(nullable = false)
    private BigDecimal saldoActual;

    @Column(nullable = false)
    private BigDecimal cuotaMinima;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    private LocalDate fechaVencimiento;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;

    public Deuda() {
    }

    public Deuda(
            Integer idDeuda,
            String nombre,
            String entidad,
            BigDecimal saldoInicial,
            BigDecimal saldoActual,
            BigDecimal cuotaMinima,
            LocalDate fechaInicio,
            LocalDate fechaVencimiento,
            Boolean activo,
            Cuenta cuenta) {
        this.idDeuda = idDeuda;
        this.nombre = nombre;
        this.entidad = entidad;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoActual;
        this.cuotaMinima = cuotaMinima;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.activo = activo;
        this.cuenta = cuenta;
    }

    public Integer getIdDeuda() {
        return idDeuda;
    }

    public void setIdDeuda(Integer idDeuda) {
        this.idDeuda = idDeuda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public BigDecimal getCuotaMinima() {
        return cuotaMinima;
    }

    public void setCuotaMinima(BigDecimal cuotaMinima) {
        this.cuotaMinima = cuotaMinima;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deuda deuda)) {
            return false;
        }
        return Objects.equals(idDeuda, deuda.idDeuda)
                && Objects.equals(nombre, deuda.nombre)
                && Objects.equals(entidad, deuda.entidad)
                && Objects.equals(saldoInicial, deuda.saldoInicial)
                && Objects.equals(saldoActual, deuda.saldoActual)
                && Objects.equals(cuotaMinima, deuda.cuotaMinima)
                && Objects.equals(fechaInicio, deuda.fechaInicio)
                && Objects.equals(fechaVencimiento, deuda.fechaVencimiento)
                && Objects.equals(activo, deuda.activo)
                && Objects.equals(cuenta, deuda.cuenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idDeuda,
                nombre,
                entidad,
                saldoInicial,
                saldoActual,
                cuotaMinima,
                fechaInicio,
                fechaVencimiento,
                activo,
                cuenta);
    }

    @Override
    public String toString() {
        return "Deuda{"
                + "idDeuda=" + idDeuda
                + ", nombre='" + nombre + '\''
                + ", entidad='" + entidad + '\''
                + ", saldoInicial=" + saldoInicial
                + ", saldoActual=" + saldoActual
                + ", cuotaMinima=" + cuotaMinima
                + ", fechaInicio=" + fechaInicio
                + ", fechaVencimiento=" + fechaVencimiento
                + ", activo=" + activo
                + ", cuenta=" + cuenta
                + '}';
    }
}
