package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.Ahorro;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AhorroRepository extends JpaRepository<Ahorro, Integer> {

    List<Ahorro> findByActivoTrue();

    List<Ahorro> findByNombre(String nombre);

    @Query("""
            select a
            from Ahorro a
            where (:activo is null or a.activo = :activo)
              and (:buscar is null or lower(a.nombre) like lower(concat('%', :buscar, '%')))
            order by
              case when a.activo = false then 1 else 0 end asc,
              (a.meta - a.ahorroActual) asc,
              a.nombre asc
            """)
    List<Ahorro> buscarConFiltros(@Param("activo") Boolean activo, @Param("buscar") String buscar);

    @Query("""
            select coalesce(sum(a.ahorroActual), 0)
            from Ahorro a
            where a.activo = true
            """)
    BigDecimal sumarAhorradoActivo();

    @Query("""
            select coalesce(sum(a.meta), 0)
            from Ahorro a
            where a.activo = true
            """)
    BigDecimal sumarObjetivosActivos();

    @Query("""
            select coalesce(sum(a.meta), 0) - coalesce(sum(a.ahorroActual), 0)
            from Ahorro a
            where a.activo = true
            """)
    BigDecimal sumarFaltanteActivo();

    Long countByActivoTrue();

    @Query("""
            select count(a)
            from Ahorro a
            where a.activo = true
              and a.ahorroActual = a.meta
            """)
    Long contarCompletadasActivas();
}
