package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.AnalisisFinancieroDTO;

public interface AnalisisFinancieroService {

    AnalisisFinancieroDTO obtenerAnalisis(String periodo, String desde, String hasta);
}
