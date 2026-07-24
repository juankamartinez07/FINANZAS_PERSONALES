package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.DashboardDTO;
import com.finanzas.finanzaspersonales.repository.DashboardRepository;
import com.finanzas.finanzaspersonales.service.DashboardService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public DashboardDTO obtenerDashboard() {
        YearMonth mesActual = YearMonth.from(LocalDate.now());
        return obtenerDashboardPorMes(mesActual.getYear(), mesActual.getMonthValue());
    }

    @Override
    public DashboardDTO obtenerDashboardPorMes(Integer anio, Integer mes) {
        YearMonth mesSeleccionado = resolverMes(anio, mes);
        LocalDate inicioMes = mesSeleccionado.atDay(1);
        LocalDate finMes = inicioMes.plusMonths(1).minusDays(1);
        BigDecimal ingresosMes = dashboardRepository.obtenerIngresosMes(inicioMes, finMes);
        BigDecimal gastosMes = dashboardRepository.obtenerGastosMes(inicioMes, finMes);
        BigDecimal balanceMes = ingresosMes.subtract(gastosMes);

        return new DashboardDTO(
                dashboardRepository.obtenerSaldoGeneral(),
                ingresosMes,
                gastosMes,
                balanceMes,
                dashboardRepository.obtenerTotalDeudasActivas(),
                dashboardRepository.obtenerTotalAhorrado(),
                dashboardRepository.contarTransaccionesPorRango(inicioMes, finMes),
                dashboardRepository.contarDeudasActivas(),
                dashboardRepository.contarMetasAhorroActivas(),
                dashboardRepository.obtenerUltimasTransaccionesPorRango(inicioMes, finMes));
    }

    private YearMonth resolverMes(Integer anio, Integer mes) {
        if (anio == null || mes == null || mes < 1 || mes > 12) {
            return YearMonth.from(LocalDate.now());
        }
        return YearMonth.of(anio, mes);
    }
}
