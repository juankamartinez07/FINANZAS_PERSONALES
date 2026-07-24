package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.ResumenTransaccionesDTO;
import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import java.time.LocalDate;
import java.util.List;

public interface TransaccionService {

    List<Transaccion> listar();

    Transaccion buscarPorId(Integer id);

    Transaccion guardar(Transaccion transaccion);

    Transaccion actualizar(Integer id, Transaccion transaccion);

    void eliminar(Integer id);

    List<Transaccion> listarPorCuenta(Integer idCuenta);

    List<Transaccion> listarPorConcepto(Integer idConcepto);

    List<Transaccion> listarPorRangoFechas(LocalDate inicio, LocalDate fin);

    List<Transaccion> listarConFiltros(
            TipoConcepto tipo,
            Integer idCuenta,
            Integer idConcepto,
            LocalDate desde,
            LocalDate hasta,
            String buscar);

    ResumenTransaccionesDTO obtenerResumenConFiltros(
            TipoConcepto tipo,
            Integer idCuenta,
            Integer idConcepto,
            LocalDate desde,
            LocalDate hasta,
            String buscar);
}
