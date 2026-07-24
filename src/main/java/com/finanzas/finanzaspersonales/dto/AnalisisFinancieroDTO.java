package com.finanzas.finanzaspersonales.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnalisisFinancieroDTO {

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String periodo;
    private String descripcionPeriodo;
    private BigDecimal totalIngresos = BigDecimal.ZERO;
    private BigDecimal totalGastos = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal porcentajeGastado = BigDecimal.ZERO;
    private BigDecimal capacidadAhorro = BigDecimal.ZERO;
    private BigDecimal porcentajeAhorro = BigDecimal.ZERO;
    private BigDecimal totalGastosFijos = BigDecimal.ZERO;
    private BigDecimal porcentajeIngresosComprometidos = BigDecimal.ZERO;
    private BigDecimal totalDeudas = BigDecimal.ZERO;
    private BigDecimal cuotaMinimaTotal = BigDecimal.ZERO;
    private BigDecimal porcentajeCuotasSobreIngresos = BigDecimal.ZERO;
    private BigDecimal totalAhorros = BigDecimal.ZERO;
    private Long cantidadTransacciones = 0L;
    private Long cantidadAlertas = 0L;
    private String nivelFinancieroGeneral;
    private String mensajeNivel;
    private String mensajeSinIngresos;
    private SugerenciaAhorroDTO sugerenciaAhorro = new SugerenciaAhorroDTO();
    private ComparacionPeriodoDTO comparacionPeriodo = new ComparacionPeriodoDTO();
    private List<IndicadorFinancieroDTO> indicadores = new ArrayList<>();
    private List<RecomendacionFinancieraDTO> recomendaciones = new ArrayList<>();
    private List<RecomendacionFinancieraDTO> recomendacionesVisibles = new ArrayList<>();
    private List<AlertaFinancieraDTO> alertas = new ArrayList<>();
    private List<DistribucionGastoDTO> principalesCategoriasGasto = new ArrayList<>();
    private List<SaldoCuentaDTO> cuentasConSaldoNegativo = new ArrayList<>();

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getDescripcionPeriodo() {
        return descripcionPeriodo;
    }

    public void setDescripcionPeriodo(String descripcionPeriodo) {
        this.descripcionPeriodo = descripcionPeriodo;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPorcentajeGastado() {
        return porcentajeGastado;
    }

    public void setPorcentajeGastado(BigDecimal porcentajeGastado) {
        this.porcentajeGastado = porcentajeGastado;
    }

    public BigDecimal getCapacidadAhorro() {
        return capacidadAhorro;
    }

    public void setCapacidadAhorro(BigDecimal capacidadAhorro) {
        this.capacidadAhorro = capacidadAhorro;
    }

    public BigDecimal getPorcentajeAhorro() {
        return porcentajeAhorro;
    }

    public void setPorcentajeAhorro(BigDecimal porcentajeAhorro) {
        this.porcentajeAhorro = porcentajeAhorro;
    }

    public BigDecimal getTotalGastosFijos() {
        return totalGastosFijos;
    }

    public void setTotalGastosFijos(BigDecimal totalGastosFijos) {
        this.totalGastosFijos = totalGastosFijos;
    }

    public BigDecimal getPorcentajeIngresosComprometidos() {
        return porcentajeIngresosComprometidos;
    }

    public void setPorcentajeIngresosComprometidos(BigDecimal porcentajeIngresosComprometidos) {
        this.porcentajeIngresosComprometidos = porcentajeIngresosComprometidos;
    }

    public BigDecimal getTotalDeudas() {
        return totalDeudas;
    }

    public void setTotalDeudas(BigDecimal totalDeudas) {
        this.totalDeudas = totalDeudas;
    }

    public BigDecimal getCuotaMinimaTotal() {
        return cuotaMinimaTotal;
    }

    public void setCuotaMinimaTotal(BigDecimal cuotaMinimaTotal) {
        this.cuotaMinimaTotal = cuotaMinimaTotal;
    }

    public BigDecimal getPorcentajeCuotasSobreIngresos() {
        return porcentajeCuotasSobreIngresos;
    }

    public void setPorcentajeCuotasSobreIngresos(BigDecimal porcentajeCuotasSobreIngresos) {
        this.porcentajeCuotasSobreIngresos = porcentajeCuotasSobreIngresos;
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

    public Long getCantidadAlertas() {
        return cantidadAlertas;
    }

    public void setCantidadAlertas(Long cantidadAlertas) {
        this.cantidadAlertas = cantidadAlertas;
    }

    public String getNivelFinancieroGeneral() {
        return nivelFinancieroGeneral;
    }

    public void setNivelFinancieroGeneral(String nivelFinancieroGeneral) {
        this.nivelFinancieroGeneral = nivelFinancieroGeneral;
    }

    public String getMensajeNivel() {
        return mensajeNivel;
    }

    public void setMensajeNivel(String mensajeNivel) {
        this.mensajeNivel = mensajeNivel;
    }

    public String getMensajeSinIngresos() {
        return mensajeSinIngresos;
    }

    public void setMensajeSinIngresos(String mensajeSinIngresos) {
        this.mensajeSinIngresos = mensajeSinIngresos;
    }

    public SugerenciaAhorroDTO getSugerenciaAhorro() {
        return sugerenciaAhorro;
    }

    public void setSugerenciaAhorro(SugerenciaAhorroDTO sugerenciaAhorro) {
        this.sugerenciaAhorro = sugerenciaAhorro;
    }

    public ComparacionPeriodoDTO getComparacionPeriodo() {
        return comparacionPeriodo;
    }

    public void setComparacionPeriodo(ComparacionPeriodoDTO comparacionPeriodo) {
        this.comparacionPeriodo = comparacionPeriodo;
    }

    public List<IndicadorFinancieroDTO> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<IndicadorFinancieroDTO> indicadores) {
        this.indicadores = indicadores;
    }

    public List<RecomendacionFinancieraDTO> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<RecomendacionFinancieraDTO> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public List<RecomendacionFinancieraDTO> getRecomendacionesVisibles() {
        return recomendacionesVisibles;
    }

    public void setRecomendacionesVisibles(List<RecomendacionFinancieraDTO> recomendacionesVisibles) {
        this.recomendacionesVisibles = recomendacionesVisibles;
    }

    public List<AlertaFinancieraDTO> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<AlertaFinancieraDTO> alertas) {
        this.alertas = alertas;
    }

    public List<DistribucionGastoDTO> getPrincipalesCategoriasGasto() {
        return principalesCategoriasGasto;
    }

    public void setPrincipalesCategoriasGasto(List<DistribucionGastoDTO> principalesCategoriasGasto) {
        this.principalesCategoriasGasto = principalesCategoriasGasto;
    }

    public List<SaldoCuentaDTO> getCuentasConSaldoNegativo() {
        return cuentasConSaldoNegativo;
    }

    public void setCuentasConSaldoNegativo(List<SaldoCuentaDTO> cuentasConSaldoNegativo) {
        this.cuentasConSaldoNegativo = cuentasConSaldoNegativo;
    }
}
