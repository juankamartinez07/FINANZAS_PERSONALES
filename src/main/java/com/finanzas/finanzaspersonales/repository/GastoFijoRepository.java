package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.GastoFijo;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GastoFijoRepository extends JpaRepository<GastoFijo, Integer> {

    List<GastoFijo> findByActivoTrue();

    List<GastoFijo> findByCuentaIdCuenta(Integer idCuenta);

    List<GastoFijo> findByDiaPago(Integer diaPago);

    @Query("""
            select g
            from GastoFijo g
            join g.cuenta c
            join g.concepto co
            where (:idCuenta is null or c.idCuenta = :idCuenta)
              and (:idConcepto is null or co.idConcepto = :idConcepto)
              and (:activo is null or g.activo = :activo)
              and (:buscar is null
                   or lower(g.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(c.nombre) like lower(concat('%', :buscar, '%'))
                   or lower(co.nombre) like lower(concat('%', :buscar, '%')))
            order by g.diaPago asc, g.valor desc, g.nombre asc
            """)
    List<GastoFijo> buscarConFiltros(
            @Param("idCuenta") Integer idCuenta,
            @Param("idConcepto") Integer idConcepto,
            @Param("activo") Boolean activo,
            @Param("buscar") String buscar);

    @Query("""
            select coalesce(sum(g.valor), 0)
            from GastoFijo g
            where g.activo = true
            """)
    BigDecimal sumarValorActivo();

    Long countByActivoTrue();

    Optional<GastoFijo> findFirstByActivoTrueOrderByValorDesc();

    @Query("select count(g) from GastoFijo g where g.concepto.idConcepto = :idConcepto")
    Long contarPorConcepto(@Param("idConcepto") Integer idConcepto);
}
