package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.Deuda;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeudaRepository extends JpaRepository<Deuda, Integer> {

    List<Deuda> findByActivoTrue();

    List<Deuda> findByCuentaIdCuenta(Integer idCuenta);

    List<Deuda> findByEntidad(String entidad);

    @Query("""
            select d
            from Deuda d
            join d.cuenta c
            where (:idCuenta is null or c.idCuenta = :idCuenta)
              and (:activo is null or d.activo = :activo)
              and (:buscar is null
                   or lower(d.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(d.entidad) like lower(concat('%', :buscar, '%'))
                   or lower(c.nombre) like lower(concat('%', :buscar, '%')))
            order by
              case when d.fechaVencimiento is null then 1 else 0 end asc,
              d.fechaVencimiento asc,
              d.saldoActual desc
            """)
    List<Deuda> buscarConFiltros(
            @Param("idCuenta") Integer idCuenta,
            @Param("activo") Boolean activo,
            @Param("buscar") String buscar);

    @Query("""
            select coalesce(sum(d.saldoActual), 0)
            from Deuda d
            where d.activo = true
              and d.saldoActual > 0
            """)
    BigDecimal sumarSaldoPendienteActivo();

    @Query("""
            select coalesce(sum(d.saldoInicial), 0) - coalesce(sum(d.saldoActual), 0)
            from Deuda d
            where d.activo = true
            """)
    BigDecimal sumarTotalPagadoActivo();

    Long countByActivoTrue();

    @Query("""
            select count(d)
            from Deuda d
            where d.activo = true
              and d.saldoActual > 0
              and d.fechaVencimiento < :fecha
            """)
    Long contarVencidas(@Param("fecha") LocalDate fecha);

    @Query("""
            select coalesce(sum(d.cuotaMinima), 0)
            from Deuda d
            where d.activo = true
            """)
    BigDecimal sumarCuotasMinimasActivas();
}
