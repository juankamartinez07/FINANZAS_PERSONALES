package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.DashboardDTO;

public interface DashboardService {

    DashboardDTO obtenerDashboard();

    DashboardDTO obtenerDashboardPorMes(Integer anio, Integer mes);
}
