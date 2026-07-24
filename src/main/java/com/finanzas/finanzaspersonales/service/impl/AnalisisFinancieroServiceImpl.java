package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.AlertaFinancieraDTO;
import com.finanzas.finanzaspersonales.dto.AnalisisFinancieroDTO;
import com.finanzas.finanzaspersonales.dto.ComparacionPeriodoDTO;
import com.finanzas.finanzaspersonales.dto.DistribucionGastoDTO;
import com.finanzas.finanzaspersonales.dto.IndicadorFinancieroDTO;
import com.finanzas.finanzaspersonales.dto.RecomendacionFinancieraDTO;
import com.finanzas.finanzaspersonales.dto.ResumenAhorrosDTO;
import com.finanzas.finanzaspersonales.dto.ResumenDeudasDTO;
import com.finanzas.finanzaspersonales.dto.ResumenGastosFijosDTO;
import com.finanzas.finanzaspersonales.dto.SaldoCuentaDTO;
import com.finanzas.finanzaspersonales.dto.SugerenciaAhorroDTO;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.repository.ReporteRepository;
import com.finanzas.finanzaspersonales.repository.TransaccionRepository;
import com.finanzas.finanzaspersonales.service.AhorroService;
import com.finanzas.finanzaspersonales.service.AnalisisFinancieroService;
import com.finanzas.finanzaspersonales.service.DeudaService;
import com.finanzas.finanzaspersonales.service.GastoFijoService;
import com.finanzas.finanzaspersonales.util.ReglasAnalisisFinanciero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AnalisisFinancieroServiceImpl implements AnalisisFinancieroService {

    private static final int ESCALA = 2;
    private static final BigDecimal CERO = BigDecimal.ZERO;

    private final ReporteRepository reporteRepository;
    private final TransaccionRepository transaccionRepository;
    private final DeudaService deudaService;
    private final AhorroService ahorroService;
    private final GastoFijoService gastoFijoService;

    public AnalisisFinancieroServiceImpl(
            ReporteRepository reporteRepository,
            TransaccionRepository transaccionRepository,
            DeudaService deudaService,
            AhorroService ahorroService,
            GastoFijoService gastoFijoService) {
        this.reporteRepository = reporteRepository;
        this.transaccionRepository = transaccionRepository;
        this.deudaService = deudaService;
        this.ahorroService = ahorroService;
        this.gastoFijoService = gastoFijoService;
    }

    @Override
    public AnalisisFinancieroDTO obtenerAnalisis(String periodo, String desde, String hasta) {
        RangoFechas rango = resolverRango(periodo, desde, hasta);
        RangoFechas rangoAnterior = calcularRangoAnterior(rango);
        BigDecimal totalIngresos = obtenerTotal(rango, TipoConcepto.INGRESO);
        BigDecimal totalGastos = obtenerTotal(rango, TipoConcepto.GASTO);
        BigDecimal balance = totalIngresos.subtract(totalGastos);
        Long cantidadTransacciones = reporteRepository.contarTransacciones(rango.desde(), rango.hasta(), null, null, null);
        ResumenDeudasDTO resumenDeudas = deudaService.obtenerResumenGeneral();
        ResumenAhorrosDTO resumenAhorros = ahorroService.obtenerResumenGeneral();
        ResumenGastosFijosDTO resumenGastosFijos = gastoFijoService.obtenerResumenGeneral();

        AnalisisFinancieroDTO analisis = new AnalisisFinancieroDTO();
        analisis.setFechaDesde(rango.desde());
        analisis.setFechaHasta(rango.hasta());
        analisis.setPeriodo(rango.periodo());
        analisis.setDescripcionPeriodo(rango.descripcion());
        analisis.setTotalIngresos(totalIngresos);
        analisis.setTotalGastos(totalGastos);
        analisis.setBalance(balance);
        analisis.setCapacidadAhorro(balance.max(CERO));
        analisis.setCantidadTransacciones(cantidadTransacciones);
        analisis.setTotalGastosFijos(obtenerBigDecimal(resumenGastosFijos.getTotalMensualComprometido()));
        analisis.setTotalDeudas(obtenerBigDecimal(resumenDeudas.getTotalSaldoPendienteActivo()));
        analisis.setCuotaMinimaTotal(obtenerBigDecimal(resumenDeudas.getTotalCuotasMinimas()));
        analisis.setTotalAhorros(obtenerBigDecimal(resumenAhorros.getTotalAhorradoActivo()));
        analisis.setMensajeSinIngresos(totalIngresos.compareTo(CERO) <= 0
                ? "No hay ingresos suficientes registrados para calcular este indicador."
                : null);

        analisis.setPorcentajeGastado(calcularPorcentaje(totalGastos, totalIngresos));
        analisis.setPorcentajeAhorro(balance.compareTo(CERO) >= 0 ? calcularPorcentaje(balance, totalIngresos) : CERO);
        analisis.setPorcentajeIngresosComprometidos(calcularPorcentaje(analisis.getTotalGastosFijos(), totalIngresos));
        analisis.setPorcentajeCuotasSobreIngresos(calcularPorcentaje(analisis.getCuotaMinimaTotal(), totalIngresos));
        analisis.setComparacionPeriodo(calcularComparacion(rango, rangoAnterior));
        analisis.setPrincipalesCategoriasGasto(obtenerPrincipalesCategorias(rango, rangoAnterior, totalGastos));
        analisis.setCuentasConSaldoNegativo(obtenerCuentasConSaldoNegativo());
        analisis.setSugerenciaAhorro(calcularSugerenciaAhorro(totalIngresos, totalGastos, balance, analisis.getPorcentajeGastado()));

        List<AlertaFinancieraDTO> alertas = crearAlertas(analisis, resumenDeudas, resumenGastosFijos);
        analisis.setAlertas(alertas);
        analisis.setCantidadAlertas((long) alertas.size());
        analisis.setNivelFinancieroGeneral(calcularNivelGeneral(analisis, resumenDeudas));
        analisis.setMensajeNivel(obtenerMensajeNivel(analisis.getNivelFinancieroGeneral()));
        analisis.setIndicadores(crearIndicadores(analisis));

        List<RecomendacionFinancieraDTO> recomendaciones = crearRecomendaciones(analisis, resumenAhorros, resumenDeudas);
        analisis.setRecomendaciones(recomendaciones);
        analisis.setRecomendacionesVisibles(recomendaciones.stream()
                .limit(ReglasAnalisisFinanciero.MAXIMO_RECOMENDACIONES_VISIBLES)
                .toList());

        return analisis;
    }

    private BigDecimal obtenerTotal(RangoFechas rango, TipoConcepto tipo) {
        return reporteRepository.sumarPorTipo(rango.desde(), rango.hasta(), null, null, null, tipo);
    }

    private ComparacionPeriodoDTO calcularComparacion(RangoFechas actual, RangoFechas anterior) {
        BigDecimal ingresosActuales = obtenerTotal(actual, TipoConcepto.INGRESO);
        BigDecimal ingresosAnteriores = obtenerTotal(anterior, TipoConcepto.INGRESO);
        BigDecimal gastosActuales = obtenerTotal(actual, TipoConcepto.GASTO);
        BigDecimal gastosAnteriores = obtenerTotal(anterior, TipoConcepto.GASTO);
        BigDecimal balanceActual = ingresosActuales.subtract(gastosActuales);
        BigDecimal balanceAnterior = ingresosAnteriores.subtract(gastosAnteriores);
        boolean comparable = ingresosAnteriores.compareTo(CERO) > 0 || gastosAnteriores.compareTo(CERO) > 0;

        return new ComparacionPeriodoDTO(
                ingresosActuales,
                ingresosAnteriores,
                ingresosActuales.subtract(ingresosAnteriores),
                porcentajeVariacion(ingresosActuales, ingresosAnteriores),
                gastosActuales,
                gastosAnteriores,
                gastosActuales.subtract(gastosAnteriores),
                porcentajeVariacion(gastosActuales, gastosAnteriores),
                balanceActual,
                balanceAnterior,
                balanceActual.subtract(balanceAnterior),
                porcentajeVariacion(balanceActual, balanceAnterior),
                comparable,
                comparable ? "Comparacion frente al periodo anterior." : "Sin datos suficientes para comparar.");
    }

    private List<DistribucionGastoDTO> obtenerPrincipalesCategorias(
            RangoFechas rango,
            RangoFechas anterior,
            BigDecimal totalGastos) {
        Map<Integer, BigDecimal> gastoAnteriorPorConcepto = new LinkedHashMap<>();
        for (Object[] fila : reporteRepository.obtenerDistribucionPorConcepto(
                anterior.desde(), anterior.hasta(), null, null, null, TipoConcepto.GASTO)) {
            gastoAnteriorPorConcepto.put(((Number) fila[0]).intValue(), obtenerBigDecimal(fila[2]));
        }

        List<DistribucionGastoDTO> categorias = new ArrayList<>();
        for (Object[] fila : reporteRepository.obtenerDistribucionPorConcepto(
                rango.desde(), rango.hasta(), null, null, null, TipoConcepto.GASTO)) {
            Integer idConcepto = ((Number) fila[0]).intValue();
            BigDecimal total = obtenerBigDecimal(fila[2]);
            categorias.add(new DistribucionGastoDTO(
                    idConcepto,
                    (String) fila[1],
                    total,
                    calcularPorcentaje(total, totalGastos),
                    obtenerLong(fila[3]),
                    total.subtract(gastoAnteriorPorConcepto.getOrDefault(idConcepto, CERO))));
        }
        return categorias.stream().limit(5).toList();
    }

    private List<SaldoCuentaDTO> obtenerCuentasConSaldoNegativo() {
        List<SaldoCuentaDTO> cuentas = new ArrayList<>();
        for (Object[] fila : transaccionRepository.obtenerCuentasConSaldoNegativo()) {
            cuentas.add(new SaldoCuentaDTO(
                    ((Number) fila[0]).intValue(),
                    (String) fila[1],
                    obtenerBigDecimal(fila[2])));
        }
        return cuentas;
    }

    private SugerenciaAhorroDTO calcularSugerenciaAhorro(
            BigDecimal totalIngresos,
            BigDecimal totalGastos,
            BigDecimal balance,
            BigDecimal porcentajeGastado) {
        if (totalIngresos.compareTo(CERO) <= 0 || balance.compareTo(CERO) <= 0) {
            return new SugerenciaAhorroDTO(CERO, CERO, "No hay margen positivo para sugerir ahorro en este periodo.");
        }

        BigDecimal porcentajeSugerido;
        String explicacion;
        if (porcentajeGastado.compareTo(ReglasAnalisisFinanciero.GASTO_ELEVADO) > 0) {
            porcentajeSugerido = new BigDecimal("5");
            explicacion = "Se sugiere una meta prudente de hasta el 5 % de los ingresos por el gasto elevado.";
        } else if (porcentajeGastado.compareTo(ReglasAnalisisFinanciero.GASTO_SALUDABLE) > 0) {
            porcentajeSugerido = new BigDecimal("10");
            explicacion = "Se sugiere hasta el 10 % de los ingresos porque los gastos ocupan una parte importante.";
        } else {
            porcentajeSugerido = ReglasAnalisisFinanciero.AHORRO_SALUDABLE;
            explicacion = "Se sugiere hasta el 20 % de los ingresos, limitado al balance disponible.";
        }

        BigDecimal valor = totalIngresos
                .multiply(porcentajeSugerido)
                .divide(ReglasAnalisisFinanciero.CIEN, ESCALA, RoundingMode.HALF_UP)
                .min(balance);
        BigDecimal porcentajeReal = calcularPorcentaje(valor, totalIngresos);
        return new SugerenciaAhorroDTO(valor, porcentajeReal, explicacion);
    }

    private List<AlertaFinancieraDTO> crearAlertas(
            AnalisisFinancieroDTO analisis,
            ResumenDeudasDTO resumenDeudas,
            ResumenGastosFijosDTO resumenGastosFijos) {
        List<AlertaFinancieraDTO> alertas = new ArrayList<>();
        if (analisis.getTotalIngresos().compareTo(CERO) <= 0) {
            alertas.add(alerta("SIN_INGRESOS", "Sin ingresos registrados",
                    "Registra ingresos para obtener indicadores completos.", "ADVERTENCIA", "bi-exclamation-circle",
                    "/transacciones/nueva"));
        }
        if (analisis.getTotalGastos().compareTo(analisis.getTotalIngresos()) > 0) {
            alertas.add(alerta("GASTOS_SUPERAN_INGRESOS", "Gastos superiores a ingresos",
                    "Los gastos del periodo superan los ingresos registrados.", "CRITICA", "bi-exclamation-octagon",
                    "/reportes?tipo=GASTO"));
        }
        if (obtenerLong(resumenDeudas.getCantidadDeudasVencidas()) > 0) {
            alertas.add(alerta("DEUDAS_VENCIDAS", "Deudas vencidas",
                    "Existen obligaciones activas con fecha de vencimiento superada.", "CRITICA", "bi-receipt",
                    "/deudas?estado=VENCIDA"));
        }
        if (obtenerLong(resumenGastosFijos.getPagosProximos()) > 0) {
            alertas.add(alerta("GASTOS_FIJOS_PROXIMOS", "Gastos fijos proximos",
                    "Hay compromisos planificados dentro de los proximos 7 dias.", "ADVERTENCIA", "bi-calendar-event",
                    "/gastosfijos?estado=PROXIMOS"));
        }
        if (!analisis.getCuentasConSaldoNegativo().isEmpty()) {
            alertas.add(alerta("CUENTAS_NEGATIVAS", "Cuentas con saldo negativo",
                    "Una o mas cuentas tienen saldo calculado negativo desde transacciones.", "ADVERTENCIA",
                    "bi-credit-card", "/cuentas"));
        }
        if (analisis.getPorcentajeCuotasSobreIngresos().compareTo(ReglasAnalisisFinanciero.CUOTAS_DEUDA_ELEVADAS) > 0) {
            alertas.add(alerta("CUOTAS_ELEVADAS", "Cuotas minimas elevadas",
                    "Las cuotas minimas representan una proporcion alta de los ingresos.", "ADVERTENCIA", "bi-bank",
                    "/deudas"));
        }
        return alertas;
    }

    private List<RecomendacionFinancieraDTO> crearRecomendaciones(
            AnalisisFinancieroDTO analisis,
            ResumenAhorrosDTO resumenAhorros,
            ResumenDeudasDTO resumenDeudas) {
        List<RecomendacionFinancieraDTO> recomendaciones = new ArrayList<>();
        if (analisis.getTotalIngresos().compareTo(CERO) <= 0) {
            recomendaciones.add(recomendacion("SIN_INGRESOS", "Registra tus ingresos",
                    "Registra tus ingresos para obtener un analisis financiero completo.", "REGISTRO", "ALTA",
                    "bi-plus-circle", "/transacciones/nueva"));
        }
        if (analisis.getTotalGastos().compareTo(analisis.getTotalIngresos()) > 0) {
            recomendaciones.add(recomendacion("GASTOS_SUPERIORES", "Revisa tus categorias principales",
                    "Tus gastos superaron tus ingresos durante el periodo. Revisa las categorias con mayor consumo.",
                    "GASTO", "ALTA", "bi-graph-down-arrow", "/reportes?tipo=GASTO"));
        } else if (analisis.getTotalIngresos().compareTo(CERO) > 0
                && analisis.getPorcentajeGastado().compareTo(ReglasAnalisisFinanciero.GASTO_ELEVADO) > 0) {
            recomendaciones.add(recomendacion("GASTOS_90_100", "Recupera margen financiero",
                    "Estas utilizando casi todos tus ingresos. Procura reducir gastos variables para recuperar margen financiero.",
                    "GASTO", "ALTA", "bi-speedometer2", "/reportes?tipo=GASTO"));
        } else if (analisis.getTotalIngresos().compareTo(CERO) > 0
                && analisis.getPorcentajeGastado().compareTo(ReglasAnalisisFinanciero.GASTO_SALUDABLE) > 0) {
            recomendaciones.add(recomendacion("GASTOS_70_90", "Revisa tus gastos principales",
                    "Tus gastos ocupan una parte importante de tus ingresos. Revisa tus categorias principales.",
                    "GASTO", "MEDIA", "bi-search", "/reportes?tipo=GASTO"));
        }
        if (analisis.getBalance().compareTo(CERO) > 0 && obtenerLong(resumenAhorros.getCantidadMetasActivas()) == 0) {
            recomendaciones.add(recomendacion("MARGEN_SIN_META", "Crea una meta de ahorro",
                    "Tienes margen disponible. Considera crear una meta de ahorro.", "AHORRO", "MEDIA",
                    "bi-piggy-bank", "/ahorros/nuevo"));
        }
        if (analisis.getTotalIngresos().compareTo(CERO) > 0
                && analisis.getBalance().compareTo(CERO) >= 0
                && analisis.getPorcentajeAhorro().compareTo(ReglasAnalisisFinanciero.AHORRO_MINIMO_RECOMENDADO) < 0) {
            recomendaciones.add(recomendacion("AHORRO_INFERIOR_10", "Mejora tu margen de ahorro",
                    "Tu margen de ahorro es inferior al 10 %. Una reduccion gradual de gastos puede ayudarte a mejorarlo.",
                    "AHORRO", "MEDIA", "bi-arrow-up-circle", "/reportes"));
        } else if (analisis.getTotalIngresos().compareTo(CERO) > 0
                && analisis.getBalance().compareTo(CERO) > 0
                && analisis.getPorcentajeAhorro().compareTo(ReglasAnalisisFinanciero.AHORRO_SALUDABLE) >= 0) {
            recomendaciones.add(recomendacion("AHORRO_SALUDABLE", "Capacidad de ahorro saludable",
                    "Tu capacidad de ahorro durante el periodo es saludable.", "AHORRO", "INFORMATIVA",
                    "bi-check-circle", "/ahorros"));
        }
        for (DistribucionGastoDTO categoria : analisis.getPrincipalesCategoriasGasto()) {
            if (categoria.getPorcentaje().compareTo(ReglasAnalisisFinanciero.CATEGORIA_CONCENTRADA) > 0) {
                recomendaciones.add(recomendacion("CATEGORIA_CONCENTRADA_" + categoria.getIdConcepto(),
                        "Gasto concentrado en " + categoria.getNombreConcepto(),
                        "Una parte importante de tus gastos se concentra en " + categoria.getNombreConcepto()
                                + ". Revisalo como una observacion del periodo.",
                        "GASTO", "MEDIA", "bi-pie-chart",
                        "/transacciones?concepto=" + categoria.getIdConcepto()));
                break;
            }
        }
        if (analisis.getPorcentajeIngresosComprometidos().compareTo(ReglasAnalisisFinanciero.GASTOS_FIJOS_ELEVADOS) > 0) {
            recomendaciones.add(recomendacion("GASTOS_FIJOS_ELEVADOS", "Compromisos planificados elevados",
                    "Tus compromisos mensuales planificados representan una proporcion elevada de tus ingresos.",
                    "PLANIFICACION", "ALTA", "bi-calendar2-check", "/gastosfijos"));
        }
        if (analisis.getPorcentajeCuotasSobreIngresos().compareTo(ReglasAnalisisFinanciero.CUOTAS_DEUDA_ELEVADAS) > 0) {
            recomendaciones.add(recomendacion("CUOTAS_MINIMAS_ELEVADAS", "Revisa tus cuotas minimas",
                    "Las cuotas minimas de tus deudas representan una parte importante de tus ingresos.",
                    "DEUDA", "ALTA", "bi-receipt", "/deudas"));
        }
        if (obtenerLong(resumenDeudas.getCantidadDeudasVencidas()) > 0) {
            recomendaciones.add(recomendacion("DEUDAS_VENCIDAS", "Revisa obligaciones vencidas",
                    "Tienes obligaciones con fecha de vencimiento superada. Revisa su estado.",
                    "DEUDA", "ALTA", "bi-exclamation-triangle", "/deudas?estado=VENCIDA"));
        }
        transaccionRepository.obtenerFechaUltimaTransaccion().ifPresent(ultimaFecha -> {
            long dias = ChronoUnit.DAYS.between(ultimaFecha, LocalDate.now());
            if (dias > ReglasAnalisisFinanciero.DIAS_SIN_TRANSACCIONES_RECIENTES) {
                recomendaciones.add(recomendacion("SIN_TRANSACCIONES_RECIENTES", "Actualiza tus movimientos",
                        "No se han registrado movimientos recientemente. Mantener tus datos actualizados mejora la precision del analisis.",
                        "REGISTRO", "BAJA", "bi-clock-history", "/transacciones/nueva"));
            }
        });
        if (analisis.getComparacionPeriodo().isComparable()
                && analisis.getComparacionPeriodo().getPorcentajeGastos() != null
                && analisis.getComparacionPeriodo().getPorcentajeGastos()
                        .compareTo(ReglasAnalisisFinanciero.VARIACION_SIGNIFICATIVA) > 0) {
            recomendaciones.add(recomendacion("GASTOS_AUMENTARON", "Gastos en aumento",
                    "Tus gastos aumentaron frente al periodo anterior. Revisa que categorias generaron el cambio.",
                    "GASTO", "MEDIA", "bi-arrow-up-right", "/reportes"));
        }
        if (analisis.getComparacionPeriodo().isComparable()
                && analisis.getComparacionPeriodo().getPorcentajeIngresos() != null
                && analisis.getComparacionPeriodo().getPorcentajeIngresos()
                        .compareTo(ReglasAnalisisFinanciero.VARIACION_SIGNIFICATIVA.negate()) < 0) {
            recomendaciones.add(recomendacion("INGRESOS_DISMINUYERON", "Ingresos en disminucion",
                    "Tus ingresos disminuyeron frente al periodo anterior. Considera ajustar temporalmente tus compromisos variables.",
                    "INGRESO", "MEDIA", "bi-arrow-down-right", "/reportes"));
        }
        return recomendaciones.stream()
                .sorted(Comparator.comparingInt(this::prioridadRecomendacion))
                .toList();
    }

    private List<IndicadorFinancieroDTO> crearIndicadores(AnalisisFinancieroDTO analisis) {
        return List.of(
                indicador("Ingresos", analisis.getTotalIngresos(), "Transacciones con conceptos de ingreso.", "bi-graph-up-arrow", "POSITIVO", false),
                indicador("Gastos", analisis.getTotalGastos(), "Transacciones con conceptos de gasto.", "bi-graph-down-arrow", "NEGATIVO", false),
                indicador("Balance", analisis.getBalance(), "Ingresos menos gastos reales del periodo.", "bi-wallet2", estadoBalance(analisis.getBalance()), false),
                indicador("Porcentaje gastado", analisis.getPorcentajeGastado(), "Gastos sobre ingresos del periodo.", "bi-percent", estadoGasto(analisis.getPorcentajeGastado()), true),
                indicador("Capacidad estimada de ahorro", analisis.getCapacidadAhorro(), "Balance positivo disponible, no registrado automaticamente.", "bi-piggy-bank", "POSITIVO", false),
                indicador("Compromisos fijos", analisis.getTotalGastosFijos(), "Gastos fijos planificados activos.", "bi-calendar2-check", estadoCompromisos(analisis.getPorcentajeIngresosComprometidos()), false),
                indicador("Cuotas minimas", analisis.getCuotaMinimaTotal(), "Cuotas minimas activas de deudas.", "bi-receipt", estadoCuotas(analisis.getPorcentajeCuotasSobreIngresos()), false),
                indicador("Deuda pendiente", analisis.getTotalDeudas(), "Saldo actual activo pendiente.", "bi-bank", "NEUTRO", false));
    }

    private String calcularNivelGeneral(AnalisisFinancieroDTO analisis, ResumenDeudasDTO resumenDeudas) {
        if (analisis.getTotalIngresos().compareTo(CERO) <= 0 && analisis.getCantidadTransacciones() == 0) {
            return "SIN DATOS";
        }
        if (analisis.getTotalGastos().compareTo(analisis.getTotalIngresos()) > 0
                || obtenerLong(resumenDeudas.getCantidadDeudasVencidas()) > 1
                || analisis.getPorcentajeCuotasSobreIngresos().compareTo(ReglasAnalisisFinanciero.CUOTAS_DEUDA_ELEVADAS) > 0) {
            return "CRITICO";
        }
        if (analisis.getPorcentajeGastado().compareTo(ReglasAnalisisFinanciero.GASTO_ELEVADO) > 0
                || analisis.getPorcentajeIngresosComprometidos().compareTo(ReglasAnalisisFinanciero.GASTOS_FIJOS_ELEVADOS) > 0) {
            return "AJUSTADO";
        }
        if (analisis.getBalance().compareTo(CERO) > 0
                && analisis.getPorcentajeGastado().compareTo(ReglasAnalisisFinanciero.GASTO_SALUDABLE) > 0) {
            return "ESTABLE";
        }
        if (analisis.getBalance().compareTo(CERO) > 0
                && analisis.getPorcentajeGastado().compareTo(ReglasAnalisisFinanciero.GASTO_SALUDABLE) <= 0) {
            return "SALUDABLE";
        }
        return "AJUSTADO";
    }

    private String obtenerMensajeNivel(String nivel) {
        return switch (nivel) {
            case "SALUDABLE" -> "Tus ingresos superan tus gastos y mantienes un margen financiero favorable.";
            case "ESTABLE" -> "Tu balance es positivo, aunque tus gastos ocupan una parte importante de tus ingresos.";
            case "AJUSTADO" -> "Tu margen financiero es reducido. Conviene revisar los gastos variables.";
            case "CRITICO" -> "Tus gastos superan tus ingresos o existen obligaciones importantes que requieren atencion.";
            default -> "Registra ingresos y gastos para obtener un analisis completo.";
        };
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
        } else {
            periodoNormalizado = "mesActual";
            YearMonth mes = YearMonth.from(hoy);
            desde = mes.atDay(1);
            hasta = mes.atEndOfMonth();
            descripcion = "Mes actual";
        }
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha inicial.");
        }
        return new RangoFechas(desde, hasta, periodoNormalizado, descripcion);
    }

    private RangoFechas calcularRangoAnterior(RangoFechas rango) {
        long dias = ChronoUnit.DAYS.between(rango.desde(), rango.hasta()) + 1;
        LocalDate hastaAnterior = rango.desde().minusDays(1);
        LocalDate desdeAnterior = hastaAnterior.minusDays(dias - 1);
        return new RangoFechas(desdeAnterior, hastaAnterior, "periodoAnterior", "Periodo anterior");
    }

    private BigDecimal calcularPorcentaje(BigDecimal valor, BigDecimal total) {
        if (total == null || total.compareTo(CERO) <= 0) {
            return CERO;
        }
        BigDecimal porcentaje = obtenerBigDecimal(valor)
                .multiply(ReglasAnalisisFinanciero.CIEN)
                .divide(total, ESCALA, RoundingMode.HALF_UP);
        return porcentaje.compareTo(CERO) < 0 ? CERO : porcentaje;
    }

    private BigDecimal porcentajeVariacion(BigDecimal actual, BigDecimal anterior) {
        if (anterior == null || anterior.compareTo(CERO) == 0) {
            return null;
        }
        return obtenerBigDecimal(actual)
                .subtract(anterior)
                .multiply(ReglasAnalisisFinanciero.CIEN)
                .divide(anterior.abs(), ESCALA, RoundingMode.HALF_UP);
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

    private AlertaFinancieraDTO alerta(String codigo, String titulo, String descripcion, String nivel, String icono, String ruta) {
        return new AlertaFinancieraDTO(codigo, titulo, descripcion, nivel, icono, ruta);
    }

    private RecomendacionFinancieraDTO recomendacion(
            String codigo,
            String titulo,
            String descripcion,
            String tipo,
            String prioridad,
            String icono,
            String ruta) {
        return new RecomendacionFinancieraDTO(codigo, titulo, descripcion, tipo, prioridad, icono, ruta);
    }

    private IndicadorFinancieroDTO indicador(
            String nombre,
            BigDecimal valor,
            String descripcion,
            String icono,
            String estadoVisual,
            boolean porcentaje) {
        return new IndicadorFinancieroDTO(nombre, obtenerBigDecimal(valor), descripcion, icono, estadoVisual, porcentaje);
    }

    private int prioridadRecomendacion(RecomendacionFinancieraDTO recomendacion) {
        return switch (recomendacion.getPrioridad()) {
            case "ALTA" -> 1;
            case "MEDIA" -> 2;
            case "BAJA" -> 3;
            default -> 4;
        };
    }

    private String estadoBalance(BigDecimal balance) {
        return balance.compareTo(CERO) < 0 ? "NEGATIVO" : "POSITIVO";
    }

    private String estadoGasto(BigDecimal porcentaje) {
        if (porcentaje.compareTo(ReglasAnalisisFinanciero.GASTO_ELEVADO) > 0) {
            return "ALTO";
        }
        if (porcentaje.compareTo(ReglasAnalisisFinanciero.GASTO_SALUDABLE) > 0) {
            return "MEDIO";
        }
        return "POSITIVO";
    }

    private String estadoCompromisos(BigDecimal porcentaje) {
        return porcentaje.compareTo(ReglasAnalisisFinanciero.GASTOS_FIJOS_ELEVADOS) > 0 ? "ALTO" : "NEUTRO";
    }

    private String estadoCuotas(BigDecimal porcentaje) {
        return porcentaje.compareTo(ReglasAnalisisFinanciero.CUOTAS_DEUDA_ELEVADAS) > 0 ? "ALTO" : "NEUTRO";
    }

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private BigDecimal obtenerBigDecimal(Object valor) {
        return valor instanceof BigDecimal bigDecimal ? bigDecimal : CERO;
    }

    private Long obtenerLong(Object valor) {
        return valor instanceof Number numero ? numero.longValue() : 0L;
    }

    private record RangoFechas(LocalDate desde, LocalDate hasta, String periodo, String descripcion) {
    }
}
