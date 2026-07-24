package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.DeudaResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenDeudasDTO;
import com.finanzas.finanzaspersonales.entity.Deuda;
import java.util.List;

public interface DeudaService {

    List<Deuda> listar();

    Deuda buscarPorId(Integer id);

    Deuda guardar(Deuda deuda);

    Deuda actualizar(Integer id, Deuda deuda);

    void eliminar(Integer id);

    List<Deuda> listarActivas();

    List<Deuda> listarPorCuenta(Integer idCuenta);

    List<Deuda> listarPorEntidad(String entidad);

    List<DeudaResumenDTO> listarResumen(String estado, Integer idCuenta, String buscar);

    DeudaResumenDTO obtenerResumenPorId(Integer id);

    ResumenDeudasDTO obtenerResumenGeneral();

    ResumenDeudasDTO obtenerResumenFiltrado(List<DeudaResumenDTO> deudas);

    Deuda cambiarEstado(Integer id);
}
