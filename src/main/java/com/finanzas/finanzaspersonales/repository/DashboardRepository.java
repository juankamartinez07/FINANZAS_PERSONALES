package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {

    private final EntityManager entityManager;

    public DashboardRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BigDecimal obtenerSaldoGeneral() {
        BigDecimal ingresos = obtenerTotalTransaccionesPorTipo(TipoConcepto.INGRESO);
        BigDecimal gastos = obtenerTotalTransaccionesPorTipo(TipoConcepto.GASTO);
        return ingresos.subtract(gastos);
    }

    public BigDecimal obtenerIngresosMes(LocalDate inicio, LocalDate fin) {
        return obtenerTotalTransaccionesPorTipoYRangoFechas(TipoConcepto.INGRESO, inicio, fin);
    }

    public BigDecimal obtenerGastosMes(LocalDate inicio, LocalDate fin) {
        return obtenerTotalTransaccionesPorTipoYRangoFechas(TipoConcepto.GASTO, inicio, fin);
    }

    public BigDecimal obtenerTotalDeudasActivas() {
        return obtenerBigDecimal(
                "select coalesce(sum(d.saldoActual), 0) from Deuda d where d.activo = true",
                BigDecimal.class);
    }

    public BigDecimal obtenerTotalAhorrado() {
        return obtenerBigDecimal(
                "select coalesce(sum(a.ahorroActual), 0) from Ahorro a where a.activo = true",
                BigDecimal.class);
    }

    public Long contarTransacciones() {
        return obtenerLong("select count(t) from Transaccion t");
    }

    public Long contarTransaccionesPorRango(LocalDate inicio, LocalDate fin) {
        Long total = entityManager.createQuery(
                        "select count(t) from Transaccion t where t.fecha between :inicio and :fin",
                        Long.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getSingleResult();
        return total != null ? total : 0L;
    }

    public Long contarDeudasActivas() {
        return obtenerLong("select count(d) from Deuda d where d.activo = true");
    }

    public Long contarMetasAhorroActivas() {
        return obtenerLong("select count(a) from Ahorro a where a.activo = true");
    }

    public List<Transaccion> obtenerUltimasTransacciones() {
        return entityManager.createQuery(
                        "select t from Transaccion t order by t.fecha desc, t.fechaRegistro desc",
                        Transaccion.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Transaccion> obtenerUltimasTransaccionesPorRango(LocalDate inicio, LocalDate fin) {
        return entityManager.createQuery(
                        "select t from Transaccion t "
                                + "where t.fecha between :inicio and :fin "
                                + "order by t.fecha desc, t.fechaRegistro desc",
                        Transaccion.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .setMaxResults(10)
                .getResultList();
    }

    private BigDecimal obtenerTotalTransaccionesPorTipo(TipoConcepto tipoConcepto) {
        BigDecimal total = entityManager.createQuery(
                        "select coalesce(sum(t.valor), 0) from Transaccion t where t.concepto.tipo = :tipo",
                        BigDecimal.class)
                .setParameter("tipo", tipoConcepto)
                .getSingleResult();
        return total != null ? total : BigDecimal.ZERO;
    }

    private BigDecimal obtenerTotalTransaccionesPorTipoYRangoFechas(
            TipoConcepto tipoConcepto,
            LocalDate inicio,
            LocalDate fin) {
        BigDecimal total = entityManager.createQuery(
                        "select coalesce(sum(t.valor), 0) "
                                + "from Transaccion t "
                                + "where t.concepto.tipo = :tipo and t.fecha between :inicio and :fin",
                        BigDecimal.class)
                .setParameter("tipo", tipoConcepto)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getSingleResult();
        return total != null ? total : BigDecimal.ZERO;
    }

    private BigDecimal obtenerBigDecimal(String consulta, Class<BigDecimal> tipoResultado) {
        BigDecimal total = entityManager.createQuery(consulta, tipoResultado).getSingleResult();
        return total != null ? total : BigDecimal.ZERO;
    }

    private Long obtenerLong(String consulta) {
        Long total = entityManager.createQuery(consulta, Long.class).getSingleResult();
        return total != null ? total : 0L;
    }
}
