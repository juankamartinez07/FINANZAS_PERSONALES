package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.ReporteDTO;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;

public interface ReporteService {

    ReporteDTO obtenerReporte(
            String periodo,
            String desde,
            String hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipo);
}
