package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.AhorroResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenAhorrosDTO;
import com.finanzas.finanzaspersonales.entity.Ahorro;
import java.util.List;

public interface AhorroService {

    List<Ahorro> listar();

    Ahorro buscarPorId(Integer id);

    Ahorro guardar(Ahorro ahorro);

    Ahorro actualizar(Integer id, Ahorro ahorro);

    void eliminar(Integer id);

    List<Ahorro> listarActivos();

    List<AhorroResumenDTO> listarResumen(String estado, String buscar);

    AhorroResumenDTO obtenerResumenPorId(Integer id);

    ResumenAhorrosDTO obtenerResumenGeneral();

    ResumenAhorrosDTO obtenerResumenFiltrado(List<AhorroResumenDTO> ahorros);

    Ahorro cambiarEstado(Integer id);
}
