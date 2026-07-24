package com.finanzas.finanzaspersonales.util;

import java.math.BigDecimal;

public final class ReglasAnalisisFinanciero {

    public static final BigDecimal CIEN = new BigDecimal("100");
    public static final BigDecimal GASTO_SALUDABLE = new BigDecimal("70");
    public static final BigDecimal GASTO_ELEVADO = new BigDecimal("90");
    public static final BigDecimal CATEGORIA_CONCENTRADA = new BigDecimal("30");
    public static final BigDecimal GASTOS_FIJOS_ELEVADOS = new BigDecimal("60");
    public static final BigDecimal CUOTAS_DEUDA_ELEVADAS = new BigDecimal("30");
    public static final BigDecimal AHORRO_MINIMO_RECOMENDADO = new BigDecimal("10");
    public static final BigDecimal AHORRO_SALUDABLE = new BigDecimal("20");
    public static final BigDecimal VARIACION_SIGNIFICATIVA = new BigDecimal("15");
    public static final int DIAS_SIN_TRANSACCIONES_RECIENTES = 15;
    public static final int MAXIMO_RECOMENDACIONES_VISIBLES = 8;

    private ReglasAnalisisFinanciero() {
    }
}
