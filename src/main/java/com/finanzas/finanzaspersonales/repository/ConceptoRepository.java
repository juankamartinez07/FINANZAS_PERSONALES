package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConceptoRepository extends JpaRepository<Concepto, Integer> {

    Optional<Concepto> findByNombre(String nombre);

    List<Concepto> findByActivoTrue();

    List<Concepto> findByActivoTrueAndTipo(TipoConcepto tipo);

    boolean existsByNombre(String nombre);

    @Query("""
            select c
            from Concepto c
            where (:tipo is null or c.tipo = :tipo)
              and (:activo is null or c.activo = :activo)
              and (:buscar is null or lower(c.nombre) like lower(concat('%', :buscar, '%')))
            order by
              case when c.activo = true then 0 else 1 end asc,
              c.tipo asc,
              c.nombre asc
            """)
    List<Concepto> buscarConFiltros(
            @Param("tipo") TipoConcepto tipo,
            @Param("activo") Boolean activo,
            @Param("buscar") String buscar);

    @Query("""
            select case when count(c) > 0 then true else false end
            from Concepto c
            where lower(trim(c.nombre)) = lower(trim(:nombre))
              and c.tipo = :tipo
            """)
    boolean existsByNombreNormalizadoAndTipo(@Param("nombre") String nombre, @Param("tipo") TipoConcepto tipo);

    @Query("""
            select case when count(c) > 0 then true else false end
            from Concepto c
            where lower(trim(c.nombre)) = lower(trim(:nombre))
              and c.tipo = :tipo
              and c.idConcepto <> :idConcepto
            """)
    boolean existsByNombreNormalizadoAndTipoAndIdConceptoNot(
            @Param("nombre") String nombre,
            @Param("tipo") TipoConcepto tipo,
            @Param("idConcepto") Integer idConcepto);

    Long countByActivoTrue();

    Long countByActivoFalse();

    Long countByTipo(TipoConcepto tipo);
}
