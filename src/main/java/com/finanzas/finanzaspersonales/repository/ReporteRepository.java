package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReporteRepository {

    private final EntityManager entityManager;

    public ReporteRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BigDecimal sumarPorTipo(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro,
            TipoConcepto tipoResumen) {
        BigDecimal total = entityManager.createQuery("""
                        select coalesce(sum(t.valor), 0)
                        from Transaccion t
                        where t.fecha between :desde and :hasta
                          and t.concepto.tipo = :tipoResumen
                          and (:tipoFiltro is null or t.concepto.tipo = :tipoFiltro)
                          and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
                          and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
                        """, BigDecimal.class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoResumen", tipoResumen)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .getSingleResult();
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long contarTransacciones(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro) {
        Long total = entityManager.createQuery("""
                        select count(t)
                        from Transaccion t
                        where t.fecha between :desde and :hasta
                          and (:tipoFiltro is null or t.concepto.tipo = :tipoFiltro)
                          and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
                          and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
                        """, Long.class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .getSingleResult();
        return total != null ? total : 0L;
    }

    public BigDecimal obtenerMayorMovimiento(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro,
            TipoConcepto tipoMovimiento) {
        BigDecimal total = entityManager.createQuery("""
                        select coalesce(max(t.valor), 0)
                        from Transaccion t
                        where t.fecha between :desde and :hasta
                          and t.concepto.tipo = :tipoMovimiento
                          and (:tipoFiltro is null or t.concepto.tipo = :tipoFiltro)
                          and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
                          and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
                        """, BigDecimal.class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoMovimiento", tipoMovimiento)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .getSingleResult();
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<Object[]> obtenerSerieMensual(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro) {
        return entityManager.createQuery("""
                        select year(t.fecha), month(t.fecha), t.concepto.tipo, coalesce(sum(t.valor), 0)
                        from Transaccion t
                        where t.fecha between :desde and :hasta
                          and (:tipoFiltro is null or t.concepto.tipo = :tipoFiltro)
                          and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
                          and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
                        group by year(t.fecha), month(t.fecha), t.concepto.tipo
                        order by year(t.fecha), month(t.fecha)
                        """, Object[].class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .getResultList();
    }

    public List<Object[]> obtenerDistribucionPorConcepto(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro,
            TipoConcepto tipoConcepto) {
        return entityManager.createQuery("""
                        select c.idConcepto, c.nombre, coalesce(sum(t.valor), 0), count(t)
                        from Transaccion t
                        join t.concepto c
                        where t.fecha between :desde and :hasta
                          and c.tipo = :tipoConcepto
                          and (:tipoFiltro is null or c.tipo = :tipoFiltro)
                          and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
                          and (:idConcepto is null or c.idConcepto = :idConcepto)
                        group by c.idConcepto, c.nombre
                        order by coalesce(sum(t.valor), 0) desc
                        """, Object[].class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoConcepto", tipoConcepto)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .getResultList();
    }

    public List<Object[]> obtenerAnalisisPorCuenta(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro) {
        return entityManager.createQuery("""
                        select cu.idCuenta, cu.nombre, t.concepto.tipo, coalesce(sum(t.valor), 0), count(t)
                        from Transaccion t
                        join t.cuenta cu
                        where t.fecha between :desde and :hasta
                          and (:tipoFiltro is null or t.concepto.tipo = :tipoFiltro)
                          and (:idCuenta is null or cu.idCuenta = :idCuenta)
                          and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
                        group by cu.idCuenta, cu.nombre, t.concepto.tipo
                        order by cu.nombre asc
                        """, Object[].class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .getResultList();
    }

    public List<Object[]> obtenerAnalisisPorConcepto(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro) {
        return entityManager.createQuery("""
                        select c.idConcepto, c.nombre, c.tipo, coalesce(sum(t.valor), 0), count(t)
                        from Transaccion t
                        join t.concepto c
                        where t.fecha between :desde and :hasta
                          and (:tipoFiltro is null or c.tipo = :tipoFiltro)
                          and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
                          and (:idConcepto is null or c.idConcepto = :idConcepto)
                        group by c.idConcepto, c.nombre, c.tipo
                        order by c.tipo asc, coalesce(sum(t.valor), 0) desc
                        """, Object[].class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .getResultList();
    }

    public List<Transaccion> obtenerTopMovimientos(
            LocalDate desde,
            LocalDate hasta,
            Integer idCuenta,
            Integer idConcepto,
            TipoConcepto tipoFiltro,
            TipoConcepto tipoMovimiento) {
        return entityManager.createQuery("""
                        select t
                        from Transaccion t
                        where t.fecha between :desde and :hasta
                          and t.concepto.tipo = :tipoMovimiento
                          and (:tipoFiltro is null or t.concepto.tipo = :tipoFiltro)
                          and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
                          and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
                        order by t.valor desc, t.fecha desc, t.fechaRegistro desc
                        """, Transaccion.class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .setParameter("tipoMovimiento", tipoMovimiento)
                .setParameter("tipoFiltro", tipoFiltro)
                .setParameter("idCuenta", idCuenta)
                .setParameter("idConcepto", idConcepto)
                .setMaxResults(5)
                .getResultList();
    }

    public List<Object[]> obtenerDeudasPorEntidad() {
        return entityManager.createQuery("""
                        select d.entidad, coalesce(sum(d.saldoActual), 0), count(d)
                        from Deuda d
                        where d.activo = true
                          and d.saldoActual > 0
                        group by d.entidad
                        order by coalesce(sum(d.saldoActual), 0) desc
                        """, Object[].class)
                .getResultList();
    }
}
