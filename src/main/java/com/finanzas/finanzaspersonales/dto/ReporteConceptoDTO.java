package com.finanzas.finanzaspersonales.dto;

import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import java.math.BigDecimal;

public class ReporteConceptoDTO {

    private final Integer idConcepto;
    private final String nombreConcepto;
    private final TipoConcepto tipo;
    private final BigDecimal total;
    private final Long cantidadMovimientos;
    private final BigDecimal promedioPorTransaccion;
    private final BigDecimal porcentajeDentroTipo;

    public ReporteConceptoDTO(
            Integer idConcepto,
            String nombreConcepto,
            TipoConcepto tipo,
            BigDecimal total,
            Long cantidadMovimientos,
            BigDecimal promedioPorTransaccion,
            BigDecimal porcentajeDentroTipo) {
        this.idConcepto = idConcepto;
        this.nombreConcepto = nombreConcepto;
        this.tipo = tipo;
        this.total = total;
        this.cantidadMovimientos = cantidadMovimientos;
        this.promedioPorTransaccion = promedioPorTransaccion;
        this.porcentajeDentroTipo = porcentajeDentroTipo;
    }

    public Integer getIdConcepto() { return idConcepto; }

    public String getNombreConcepto() { return nombreConcepto; }

    public TipoConcepto getTipo() { return tipo; }

    public BigDecimal getTotal() { return total; }

    public Long getCantidadMovimientos() { return cantidadMovimientos; }

    public BigDecimal getPromedioPorTransaccion() { return promedioPorTransaccion; }

    public BigDecimal getPorcentajeDentroTipo() { return porcentajeDentroTipo; }
}
