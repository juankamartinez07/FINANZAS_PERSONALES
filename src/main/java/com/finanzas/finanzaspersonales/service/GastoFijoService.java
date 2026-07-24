package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.GastoFijoResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenGastosFijosDTO;
import com.finanzas.finanzaspersonales.entity.GastoFijo;
import java.util.List;

public interface GastoFijoService {

    List<GastoFijo> listar();

    GastoFijo buscarPorId(Integer id);

    GastoFijo guardar(GastoFijo gastoFijo);

    GastoFijo actualizar(Integer id, GastoFijo gastoFijo);

    void eliminar(Integer id);

    List<GastoFijo> listarActivos();

    List<GastoFijo> listarPorCuenta(Integer idCuenta);

    List<GastoFijo> listarPorDiaPago(Integer diaPago);

    List<GastoFijoResumenDTO> listarResumen(String estado, Integer idCuenta, Integer idConcepto, String buscar);

    GastoFijoResumenDTO obtenerResumenPorId(Integer id);

    ResumenGastosFijosDTO obtenerResumenGeneral();

    ResumenGastosFijosDTO obtenerResumenFiltrado(List<GastoFijoResumenDTO> gastosFijos);

    GastoFijo cambiarEstado(Integer id);
}
