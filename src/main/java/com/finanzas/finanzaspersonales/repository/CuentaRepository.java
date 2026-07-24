package com.finanzas.finanzaspersonales.repository;

import com.finanzas.finanzaspersonales.entity.Cuenta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {

    List<Cuenta> findByActivoTrue();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCuentaNot(String nombre, Integer idCuenta);

    Long countByActivoTrue();

    @Query("""
            select c
            from Cuenta c
            where (:activo is null or c.activo = :activo)
              and (:buscar is null or lower(c.nombre) like lower(concat('%', :buscar, '%')))
            order by c.nombre asc
            """)
    List<Cuenta> buscarPorNombreYEstado(@Param("buscar") String buscar, @Param("activo") Boolean activo);
}
