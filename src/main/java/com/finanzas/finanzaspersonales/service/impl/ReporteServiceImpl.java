package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.ComparacionMensualDTO;
import com.finanzas.finanzaspersonales.dto.DistribucionDTO;
import com.finanzas.finanzaspersonales.dto.ReporteConceptoDTO;
import com.finanzas.finanzaspersonales.dto.ReporteCuentaDTO;
import com.finanzas.finanzaspersonales.dto.ReporteDTO;
import com.finanzas.finanzaspersonales.dto.ResumenPeriodoDTO;
import com.finanzas.finanzaspersonales.dto.SerieMensualDTO;
import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.repository.ReporteRepository;
import com.finanzas.finanzaspersonales.service.AhorroService;
import com.finanzas.finanzaspersonales.service.DeudaService;
import com.finanzas.finanzaspersonales.service.GastoFijoService;
import com.finanzas.finanzaspersonales.service.ReporteService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final BigDecimal CIEN = new BigDecimal("100");

    private final ReporteRepository reporteRepository;
    private final DeudaService deudaService;
    private final AhorroService ahorroService;
    private final GastoFijoService gastoFijoService;

    public ReporteServiceImpl(
            ReporteRepository reporteRepository,
            DeudaService deudaService,
            AhorroService ahorroService,
            GastoFijoService gastoFijoService) {
        this.reporteRepository = reporteRepository;
        this.deudaService = deudaService;
        this.ahorroService = ahorroService;
        this.gastoFijoService = gastoFijoService;
    }

    @Override
    public ReporteDTO obtenerReporte(
            String periodo,
            String desde,
            String hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipo) {
        RangoFechas rango = resolverRango(periodo, desde, hasta);
        BigDecimal ingresos = reporteRepository.sumarPorTipo(
                rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.INGRESO);
        BigDecimal gastos = reporteRepository.sumarPorTipo(
                rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.GASTO);
        Long cantidad = reporteRepository.contarTransacciones(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo);
        BigDecimal balance = ingresos.subtract(gastos);
        BigDecimal divisorPromedio = BigDecimal.valueOf(calcularCantidadMeses(rango.desde(), rango.hasta()));

        ResumenPeriodoDTO resumen = new ResumenPeriodoDTO(
                ingresos,
                gastos,
                balance,
                dividir(ingresos, divisorPromedio),
                dividir(gastos, divisorPromedio),
                cantidad,
                reporteRepository.obtenerMayorMovimiento(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.INGRESO),
                reporteRepository.obtenerMayorMovimiento(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.GASTO));

        List<DistribucionDTO> gastosPorConcepto = convertirDistribucion(
                reporteRepository.obtenerDistribucionPorConcepto(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.GASTO),
                gastos);
        List<DistribucionDTO> ingresosPorConcepto = convertirDistribucion(
                reporteRepository.obtenerDistribucionPorConcepto(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.INGRESO),
                ingresos);

        return new ReporteDTO(
                resumen,
                obtenerComparacionMensual(idCuenta, idConcepto, tipo),
                obtenerSerieMensual(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo),
                gastosPorConcepto,
                ingresosPorConcepto,
                obtenerReporteCuentas(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, cantidad),
                obtenerReporteConceptos(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, ingresos, gastos),
                reporteRepository.obtenerTopMovimientos(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.INGRESO),
                reporteRepository.obtenerTopMovimientos(rango.desde(), rango.hasta(), idCuenta, idConcepto, tipo, TipoConcepto.GASTO),
                deudaService.obtenerResumenGeneral(),
                ahorroService.obtenerResumenGeneral(),
                gastoFijoService.obtenerResumenGeneral(),
                obtenerDeudasPorEntidad(),
                rango.desde(),
                rango.hasta(),
                rango.periodo(),
                idCuenta,
                idConcepto,
                tipo,
                rango.descripcion());
    }

    private ComparacionMensualDTO obtenerComparacionMensual(Integer idCuenta, Integer idConcepto, TipoConcepto tipo) {
        LocalDate inicioMesActual = LocalDate.now().withDayOfMonth(1);
        LocalDate finMesActual = inicioMesActual.plusMonths(1).minusDays(1);
        LocalDate inicioMesAnterior = inicioMesActual.minusMonths(1);
        LocalDate finMesAnterior = inicioMesActual.minusDays(1);

        BigDecimal ingresosActual = reporteRepository.sumarPorTipo(inicioMesActual, finMesActual, idCuenta, idConcepto, tipo, TipoConcepto.INGRESO);
        BigDecimal ingresosAnterior = reporteRepository.sumarPorTipo(inicioMesAnterior, finMesAnterior, idCuenta, idConcepto, tipo, TipoConcepto.INGRESO);
        BigDecimal gastosActual = reporteRepository.sumarPorTipo(inicioMesActual, finMesActual, idCuenta, idConcepto, tipo, TipoConcepto.GASTO);
        BigDecimal gastosAnterior = reporteRepository.sumarPorTipo(inicioMesAnterior, finMesAnterior, idCuenta, idConcepto, tipo, TipoConcepto.GASTO);
        BigDecimal balanceActual = ingresosActual.subtract(gastosActual);
        BigDecimal balanceAnterior = ingresosAnterior.subtract(gastosAnterior);

        return new ComparacionMensualDTO(
                ingresosActual,
                ingresosAnterior,
                ingresosActual.subtract(ingresosAnterior),
                porcentajeVariacion(ingresosActual, ingresosAnterior),
                gastosActual,
                gastosAnterior,
                gastosActual.subtract(gastosAnterior),
                porcentajeVariacion(gastosActual, gastosAnterior),
                balanceActual,
                balanceAnterior,
                balanceActual.subtract(balanceAnterior),
                porcentajeVariacion(balanceActual, balanceAnterior));
    }

    private List<SerieMensualDTO> obtenerSerieMensual(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipo) {
        YearMonth inicio = YearMonth.from(desde);
        YearMonth fin = YearMonth.from(hasta);
        if (ChronoUnit.MONTHS.between(inicio, fin) >= 12) {
            inicio = fin.minusMonths(11);
        }

        Map<YearMonth, BigDecimal[]> serie = new LinkedHashMap<>();
        YearMonth cursor = inicio;
        while (!cursor.isAfter(fin)) {
            serie.put(cursor, new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO});
            cursor = cursor.plusMonths(1);
        }

        for (Object[] fila : reporteRepository.obtenerSerieMensual(inicio.atDay(1), hasta, idCuenta, idConcepto, tipo)) {
            YearMonth mes = YearMonth.of(((Number) fila[0]).intValue(), ((Number) fila[1]).intValue());
            BigDecimal[] valores = serie.get(mes);
            if (valores != null) {
                TipoConcepto tipoConcepto = (TipoConcepto) fila[2];
                if (tipoConcepto == TipoConcepto.INGRESO) {
                    valores[0] = obtenerBigDecimal(fila[3]);
                } else {
                    valores[1] = obtenerBigDecimal(fila[3]);
                }
            }
        }

        List<SerieMensualDTO> resultado = new ArrayList<>();
        for (Map.Entry<YearMonth, BigDecimal[]> entrada : serie.entrySet()) {
            BigDecimal ingresos = entrada.getValue()[0];
            BigDecimal gastos = entrada.getValue()[1];
            resultado.add(new SerieMensualDTO(
                    entrada.getKey().toString(),
                    ingresos,
                    gastos,
                    ingresos.subtract(gastos)));
        }
        return resultado;
    }

    private List<DistribucionDTO> convertirDistribucion(List<Object[]> filas, BigDecimal totalTipo) {
        List<DistribucionDTO> resultado = new ArrayList<>();
        BigDecimal totalOtros = BigDecimal.ZERO;
        long cantidadOtros = 0L;
        int limite = 8;
        for (int i = 0; i < filas.size(); i++) {
            Object[] fila = filas.get(i);
            BigDecimal total = obtenerBigDecimal(fila[2]);
            Long cantidad = obtenerLong(fila[3]);
            if (i < limite) {
                resultado.add(new DistribucionDTO(
                        ((Number) fila[0]).intValue(),
                        (String) fila[1],
                        total,
                        porcentaje(total, totalTipo),
                        cantidad));
            } else {
                totalOtros = totalOtros.add(total);
                cantidadOtros += cantidad;
            }
        }
        if (totalOtros.compareTo(BigDecimal.ZERO) > 0) {
            resultado.add(new DistribucionDTO(null, "Otros", totalOtros, porcentaje(totalOtros, totalTipo), cantidadOtros));
        }
        return resultado;
    }

    private List<ReporteCuentaDTO> obtenerReporteCuentas(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipo,
            Long totalMovimientos) {
        Map<Integer, CuentaAcumulada> acumuladas = new LinkedHashMap<>();
        for (Object[] fila : reporteRepository.obtenerAnalisisPorCuenta(desde, hasta, idCuenta, idConcepto, tipo)) {
            Integer id = ((Number) fila[0]).intValue();
            CuentaAcumulada acumulada = acumuladas.computeIfAbsent(id, key -> new CuentaAcumulada(id, (String) fila[1]));
            TipoConcepto tipoConcepto = (TipoConcepto) fila[2];
            if (tipoConcepto == TipoConcepto.INGRESO) {
                acumulada.ingresos = acumulada.ingresos.add(obtenerBigDecimal(fila[3]));
            } else {
                acumulada.gastos = acumulada.gastos.add(obtenerBigDecimal(fila[3]));
            }
            acumulada.cantidad += obtenerLong(fila[4]);
        }
        return acumuladas.values().stream()
                .map(cuenta -> new ReporteCuentaDTO(
                        cuenta.id,
                        cuenta.nombre,
                        cuenta.ingresos,
                        cuenta.gastos,
                        cuenta.ingresos.subtract(cuenta.gastos),
                        cuenta.cantidad,
                        porcentaje(BigDecimal.valueOf(cuenta.cantidad), BigDecimal.valueOf(totalMovimientos))))
                .toList();
    }

    private List<ReporteConceptoDTO> obtenerReporteConceptos(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipo,
            BigDecimal ingresos,
            BigDecimal gastos) {
        List<ReporteConceptoDTO> resultado = new ArrayList<>();
        for (Object[] fila : reporteRepository.obtenerAnalisisPorConcepto(desde, hasta, idCuenta, idConcepto, tipo)) {
            TipoConcepto tipoConcepto = (TipoConcepto) fila[2];
            BigDecimal total = obtenerBigDecimal(fila[3]);
            Long cantidad = obtenerLong(fila[4]);
            resultado.add(new ReporteConceptoDTO(
                    ((Number) fila[0]).intValue(),
                    (String) fila[1],
                    tipoConcepto,
                    total,
                    cantidad,
                    dividir(total, BigDecimal.valueOf(Math.max(cantidad, 1L))),
                    porcentaje(total, tipoConcepto == TipoConcepto.INGRESO ? ingresos : gastos)));
        }
        return resultado;
    }

    private List<DistribucionDTO> obtenerDeudasPorEntidad() {
        BigDecimal total = BigDecimal.ZERO;
        List<Object[]> filas = reporteRepository.obtenerDeudasPorEntidad();
        for (Object[] fila : filas) {
            total = total.add(obtenerBigDecimal(fila[1]));
        }
        List<DistribucionDTO> resultado = new ArrayList<>();
        for (int i = 0; i < filas.size(); i++) {
            Object[] fila = filas.get(i);
            BigDecimal valor = obtenerBigDecimal(fila[1]);
            resultado.add(new DistribucionDTO(i + 1, (String) fila[0], valor, porcentaje(valor, total), obtenerLong(fila[2])));
        }
        return resultado;
    }

    private RangoFechas resolverRango(String periodo, String desdeTexto, String hastaTexto) {
        String periodoNormalizado = periodo == null || periodo.trim().isEmpty() ? "mesActual" : periodo.trim();
        LocalDate hoy = LocalDate.now();
        LocalDate desde;
        LocalDate hasta;
        String descripcion;

        if ("personalizado".equals(periodoNormalizado) || tieneTexto(desdeTexto) || tieneTexto(hastaTexto)) {
            desde = parsearFecha(desdeTexto, "La fecha inicial no es valida.");
            hasta = parsearFecha(hastaTexto, "La fecha final no es valida.");
            periodoNormalizado = "personalizado";
            descripcion = "Rango personalizado";
        } else if ("mesAnterior".equals(periodoNormalizado)) {
            YearMonth mes = YearMonth.from(hoy).minusMonths(1);
            desde = mes.atDay(1);
            hasta = mes.atEndOfMonth();
            descripcion = "Mes anterior";
        } else if ("ultimos3Meses".equals(periodoNormalizado)) {
            YearMonth finMes = YearMonth.from(hoy);
            desde = finMes.minusMonths(2).atDay(1);
            hasta = finMes.atEndOfMonth();
            descripcion = "Ultimos 3 meses";
        } else if ("ultimos6Meses".equals(periodoNormalizado)) {
            YearMonth finMes = YearMonth.from(hoy);
            desde = finMes.minusMonths(5).atDay(1);
            hasta = finMes.atEndOfMonth();
            descripcion = "Ultimos 6 meses";
        } else if ("anioActual".equals(periodoNormalizado)) {
            desde = hoy.withDayOfYear(1);
            hasta = hoy;
            descripcion = "Anio actual";
        } else if ("ultimos12Meses".equals(periodoNormalizado)) {
            YearMonth finMes = YearMonth.from(hoy);
            desde = finMes.minusMonths(11).atDay(1);
            hasta = finMes.atEndOfMonth();
            descripcion = "Ultimos 12 meses";
        } else {
            periodoNormalizado = "mesActual";
            desde = hoy.withDayOfMonth(1);
            hasta = hoy.withDayOfMonth(hoy.lengthOfMonth());
            descripcion = "Mes actual";
        }

        validarRango(desde, hasta);
        return new RangoFechas(desde, hasta, periodoNormalizado, descripcion);
    }

    private LocalDate parsearFecha(String valor, String mensaje) {
        if (!tieneTexto(valor)) {
            throw new IllegalArgumentException(mensaje);
        }
        try {
            return LocalDate.parse(valor.trim());
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private void validarRango(LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha inicial.");
        }
        if (ChronoUnit.YEARS.between(desde, hasta) > 5) {
            throw new IllegalArgumentException("El rango maximo permitido para reportes es de 5 anios.");
        }
    }

    private long calcularCantidadMeses(LocalDate desde, LocalDate hasta) {
        return Math.max(1, ChronoUnit.MONTHS.between(YearMonth.from(desde), YearMonth.from(hasta)) + 1);
    }

    private BigDecimal porcentajeVariacion(BigDecimal actual, BigDecimal anterior) {
        if (anterior == null || anterior.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return actual.subtract(anterior).multiply(CIEN).divide(anterior.abs(), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal porcentaje(BigDecimal valor, BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valor.multiply(CIEN).divide(total, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal dividir(BigDecimal valor, BigDecimal divisor) {
        if (divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valor.divide(divisor, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal obtenerBigDecimal(Object valor) {
        return valor instanceof BigDecimal bigDecimal ? bigDecimal : BigDecimal.ZERO;
    }

    private Long obtenerLong(Object valor) {
        return valor instanceof Number numero ? numero.longValue() : 0L;
    }

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private record RangoFechas(LocalDate desde, LocalDate hasta, String periodo, String descripcion) {
    }

    private static class CuentaAcumulada {
        private final Integer id;
        private final String nombre;
        private BigDecimal ingresos = BigDecimal.ZERO;
        private BigDecimal gastos = BigDecimal.ZERO;
        private Long cantidad = 0L;

        private CuentaAcumulada(Integer id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }
}
