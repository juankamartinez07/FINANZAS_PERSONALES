package com.finanzas.finanzaspersonales.service;

import com.finanzas.finanzaspersonales.dto.ConceptoResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenConceptosDTO;
import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import java.util.List;

public interface ConceptoService {

    List<Concepto> listar();

    Concepto buscarPorId(Integer id);

    Concepto guardar(Concepto concepto);

    Concepto actualizar(Integer id, Concepto concepto);

    void eliminar(Integer id);

    List<ConceptoResumenDTO> listarResumen(TipoConcepto tipo, Boolean activo, String buscar);

    ConceptoResumenDTO obtenerResumenPorId(Integer id);

    ResumenConceptosDTO obtenerResumenGeneral();

    Concepto cambiarEstado(Integer id);

    boolean puedeCambiarTipo(Integer id);

    List<Transaccion> obtenerUltimasTransacciones(Integer id);
}
