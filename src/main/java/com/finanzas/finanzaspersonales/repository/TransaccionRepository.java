package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    List<Transaccion> findByCuentaIdCuenta(Integer idCuenta);

    List<Transaccion> findByConceptoIdConcepto(Integer idConcepto);

    List<Transaccion> findByFechaBetween(LocalDate inicio, LocalDate fin);

    @Query("""
            select t
            from Transaccion t
            where (:tipo is null or t.concepto.tipo = :tipo)
              and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
              and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
              and (:desde is null or t.fecha >= :desde)
              and (:hasta is null or t.fecha <= :hasta)
              and (:buscar is null
                   or lower(t.concepto.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(t.cuenta.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(coalesce(t.observacion, '')) like lower(concat('%', :buscar, '%')))
            order by t.fecha desc, t.fechaRegistro desc
            """)
    List<Transaccion> buscarConFiltros(
            @Param("tipo") TipoConcepto tipo,
            @Param("idCuenta") Integer idCuenta,
            @Param("idConcepto") Integer idConcepto,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("buscar") String buscar);

    @Query("""
            select coalesce(sum(t.valor), 0)
            from Transaccion t
            where t.concepto.tipo = :tipoResumen
              and (:tipo is null or t.concepto.tipo = :tipo)
              and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
              and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
              and (:desde is null or t.fecha >= :desde)
              and (:hasta is null or t.fecha <= :hasta)
              and (:buscar is null
                   or lower(t.concepto.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(t.cuenta.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(coalesce(t.observacion, '')) like lower(concat('%', :buscar, '%')))
            """)
    BigDecimal sumarPorTipoConFiltros(
            @Param("tipoResumen") TipoConcepto tipoResumen,
            @Param("tipo") TipoConcepto tipo,
            @Param("idCuenta") Integer idCuenta,
            @Param("idConcepto") Integer idConcepto,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("buscar") String buscar);

    @Query("""
            select count(t)
            from Transaccion t
            where (:tipo is null or t.concepto.tipo = :tipo)
              and (:idCuenta is null or t.cuenta.idCuenta = :idCuenta)
              and (:idConcepto is null or t.concepto.idConcepto = :idConcepto)
              and (:desde is null or t.fecha >= :desde)
              and (:hasta is null or t.fecha <= :hasta)
              and (:buscar is null
                   or lower(t.concepto.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(t.cuenta.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(coalesce(t.observacion, '')) like lower(concat('%', :buscar, '%')))
            """)
    Long contarConFiltros(
            @Param("tipo") TipoConcepto tipo,
            @Param("idCuenta") Integer idCuenta,
            @Param("idConcepto") Integer idConcepto,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("buscar") String buscar);

    @Query("""
            select coalesce(sum(t.valor), 0)
            from Transaccion t
            where t.cuenta.idCuenta = :idCuenta
              and t.concepto.tipo = :tipo
            """)
    BigDecimal sumarPorCuentaYTipo(@Param("idCuenta") Integer idCuenta, @Param("tipo") TipoConcepto tipo);

    @Query("select count(t) from Transaccion t where t.cuenta.idCuenta = :idCuenta")
    Long contarPorCuenta(@Param("idCuenta") Integer idCuenta);

    @Query("select count(t) from Transaccion t where t.concepto.idConcepto = :idConcepto")
    Long contarPorConcepto(@Param("idConcepto") Integer idConcepto);

    List<Transaccion> findTop10ByCuentaIdCuentaOrderByFechaDescFechaRegistroDesc(Integer idCuenta);

    List<Transaccion> findTop10ByConceptoIdConceptoOrderByFechaDescFechaRegistroDesc(Integer idConcepto);

    @Query("""
            select c.idCuenta,
                   c.nombre,
                   coalesce(sum(case when co.tipo = com.finanzas.finanzaspersonales.entity.enums.TipoConcepto.INGRESO then t.valor else 0 end), 0)
                   - coalesce(sum(case when co.tipo = com.finanzas.finanzaspersonales.entity.enums.TipoConcepto.GASTO then t.valor else 0 end), 0)
            from Transaccion t
            join t.cuenta c
            join t.concepto co
            group by c.idCuenta, c.nombre
            having coalesce(sum(case when co.tipo = com.finanzas.finanzaspersonales.entity.enums.TipoConcepto.INGRESO then t.valor else 0 end), 0)
                   - coalesce(sum(case when co.tipo = com.finanzas.finanzaspersonales.entity.enums.TipoConcepto.GASTO then t.valor else 0 end), 0) < 0
            order by 3 asc
            """)
    List<Object[]> obtenerCuentasConSaldoNegativo();

    @Query("select max(t.fecha) from Transaccion t")
    Optional<LocalDate> obtenerFechaUltimaTransaccion();
}
