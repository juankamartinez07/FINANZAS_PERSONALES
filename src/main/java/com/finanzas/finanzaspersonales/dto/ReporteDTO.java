package com.finanzas.finanzaspersonales.dto;

import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import java.time.LocalDate;
import java.util.List;

public class ReporteDTO {

    private final ResumenPeriodoDTO resumen;
    private final ComparacionMensualDTO comparacion;
    private final List<SerieMensualDTO> evolucionMensual;
    private final List<DistribucionDTO> gastosPorConcepto;
    private final List<DistribucionDTO> ingresosPorConcepto;
    private final List<ReporteCuentaDTO> cuentas;
    private final List<ReporteConceptoDTO> conceptos;
    private final List<Transaccion> mayoresIngresos;
    private final List<Transaccion> mayoresGastos;
    private final ResumenDeudasDTO resumenDeudas;
    private final ResumenAhorrosDTO resumenAhorros;
    private final ResumenGastosFijosDTO resumenGastosFijos;
    private final List<DistribucionDTO> deudasPorEntidad;
    private final LocalDate desde;
    private final LocalDate hasta;
    private final String periodo;
    private final Integer cuentaSeleccionada;
    private final Integer conceptoSeleccionado;
    private final TipoConcepto tipoSeleccionado;
    private final String descripcionPeriodo;

    public ReporteDTO(
            ResumenPeriodoDTO resumen,
            ComparacionMensualDTO comparacion,
            List<SerieMensualDTO> evolucionMensual,
            List<DistribucionDTO> gastosPorConcepto,
            List<DistribucionDTO> ingresosPorConcepto,
            List<ReporteCuentaDTO> cuentas,
            List<ReporteConceptoDTO> conceptos,
            List<Transaccion> mayoresIngresos,
            List<Transaccion> mayoresGastos,
            ResumenDeudasDTO resumenDeudas,
            ResumenAhorrosDTO resumenAhorros,
            ResumenGastosFijosDTO resumenGastosFijos,
            List<DistribucionDTO> deudasPorEntidad,
            LocalDate desde,
            LocalDate hasta,
            String periodo,
            Integer cuentaSeleccionada,
            Integer conceptoSeleccionado,
            TipoConcepto tipoSeleccionado,
            String descripcionPeriodo) {
        this.resumen = resumen;
        this.comparacion = comparacion;
        this.evolucionMensual = evolucionMensual;
        this.gastosPorConcepto = gastosPorConcepto;
        this.ingresosPorConcepto = ingresosPorConcepto;
        this.cuentas = cuentas;
        this.conceptos = conceptos;
        this.mayoresIngresos = mayoresIngresos;
        this.mayoresGastos = mayoresGastos;
        this.resumenDeudas = resumenDeudas;
        this.resumenAhorros = resumenAhorros;
        this.resumenGastosFijos = resumenGastosFijos;
        this.deudasPorEntidad = deudasPorEntidad;
        this.desde = desde;
        this.hasta = hasta;
        this.periodo = periodo;
        this.cuentaSeleccionada = cuentaSeleccionada;
        this.conceptoSeleccionado = conceptoSeleccionado;
        this.tipoSeleccionado = tipoSeleccionado;
        this.descripcionPeriodo = descripcionPeriodo;
    }

    public ResumenPeriodoDTO getResumen() { return resumen; }

    public ComparacionMensualDTO getComparacion() { return comparacion; }

    public List<SerieMensualDTO> getEvolucionMensual() { return evolucionMensual; }

    public List<DistribucionDTO> getGastosPorConcepto() { return gastosPorConcepto; }

    public List<DistribucionDTO> getIngresosPorConcepto() { return ingresosPorConcepto; }

    public List<ReporteCuentaDTO> getCuentas() { return cuentas; }

    public List<ReporteConceptoDTO> getConceptos() { return conceptos; }

    public List<Transaccion> getMayoresIngresos() { return mayoresIngresos; }

    public List<Transaccion> getMayoresGastos() { return mayoresGastos; }

    public ResumenDeudasDTO getResumenDeudas() { return resumenDeudas; }

    public ResumenAhorrosDTO getResumenAhorros() { return resumenAhorros; }

    public ResumenGastosFijosDTO getResumenGastosFijos() { return resumenGastosFijos; }

    public List<DistribucionDTO> getDeudasPorEntidad() { return deudasPorEntidad; }

    public LocalDate getDesde() { return desde; }

    public LocalDate getHasta() { return hasta; }

    public String getPeriodo() { return periodo; }

    public Integer getCuentaSeleccionada() { return cuentaSeleccionada; }

    public Integer getConceptoSeleccionado() { return conceptoSeleccionado; }

    public TipoConcepto getTipoSeleccionado() { return tipoSeleccionado; }

    public String getDescripcionPeriodo() { return descripcionPeriodo; }
}
