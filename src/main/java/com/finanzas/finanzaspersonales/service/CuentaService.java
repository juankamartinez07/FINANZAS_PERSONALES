package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.CuentaDetalleDTO;
import com.finanzas.finanzaspersonales.dto.CuentaResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenCuentasDTO;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import java.util.List;

public interface CuentaService {

    List<CuentaResumenDTO> listarResumen(String buscar, Boolean activo);

    ResumenCuentasDTO obtenerResumenGeneral(List<CuentaResumenDTO> cuentas);

    Cuenta buscarPorId(Integer id);

    CuentaDetalleDTO obtenerDetalle(Integer id);

    Cuenta guardar(Cuenta cuenta);

    Cuenta actualizar(Integer id, Cuenta cuenta);

    Cuenta cambiarEstado(Integer id);
}
