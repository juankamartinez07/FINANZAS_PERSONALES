package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.ConceptoResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenConceptosDTO;
import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.exception.ConceptoNoEncontradoException;
import com.finanzas.finanzaspersonales.repository.ConceptoRepository;
import com.finanzas.finanzaspersonales.repository.GastoFijoRepository;
import com.finanzas.finanzaspersonales.repository.TransaccionRepository;
import com.finanzas.finanzaspersonales.service.ConceptoService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConceptoServiceImpl implements ConceptoService {

    private final ConceptoRepository conceptoRepository;
    private final TransaccionRepository transaccionRepository;
    private final GastoFijoRepository gastoFijoRepository;

    public ConceptoServiceImpl(
            ConceptoRepository conceptoRepository,
            TransaccionRepository transaccionRepository,
            GastoFijoRepository gastoFijoRepository) {
        this.conceptoRepository = conceptoRepository;
        this.transaccionRepository = transaccionRepository;
        this.gastoFijoRepository = gastoFijoRepository;
    }

    @Override
    public List<Concepto> listar() {
        return conceptoRepository.findAll();
    }

    @Override
    public Concepto buscarPorId(Integer id) {
        return conceptoRepository.findById(id)
                .orElseThrow(() -> new ConceptoNoEncontradoException("Concepto no encontrado"));
    }

    @Override
    public Concepto guardar(Concepto concepto) {
        validarConcepto(concepto, null);
        concepto.setActivo(true);
        return conceptoRepository.save(concepto);
    }

    @Override
    public Concepto actualizar(Integer id, Concepto concepto) {
        Concepto conceptoExistente = buscarPorId(id);
        validarConcepto(concepto, id);
        validarCambioTipo(conceptoExistente, concepto);
        conceptoExistente.setNombre(concepto.getNombre());
        conceptoExistente.setTipo(concepto.getTipo());
        conceptoExistente.setActivo(concepto.getActivo() != null ? concepto.getActivo() : conceptoExistente.getActivo());
        return conceptoRepository.save(conceptoExistente);
    }

    @Override
    public void eliminar(Integer id) {
        Concepto concepto = buscarPorId(id);
        if (tieneTransacciones(id) || tieneGastosFijos(id)) {
            throw new IllegalArgumentException("No es posible eliminar un concepto con registros asociados.");
        }
        conceptoRepository.delete(concepto);
    }

    @Override
    public List<ConceptoResumenDTO> listarResumen(TipoConcepto tipo, Boolean activo, String buscar) {
        return conceptoRepository.buscarConFiltros(tipo, activo, normalizarTextoOpcional(buscar)).stream()
                .map(this::convertirAResumen)
                .toList();
    }

    @Override
    public ConceptoResumenDTO obtenerResumenPorId(Integer id) {
        return convertirAResumen(buscarPorId(id));
    }

    @Override
    public ResumenConceptosDTO obtenerResumenGeneral() {
        long total = conceptoRepository.count();
        long activos = obtenerLong(conceptoRepository.countByActivoTrue());
        long ingresos = obtenerLong(conceptoRepository.countByTipo(TipoConcepto.INGRESO));
        long gastos = obtenerLong(conceptoRepository.countByTipo(TipoConcepto.GASTO));
        long inactivos = obtenerLong(conceptoRepository.countByActivoFalse());
        return new ResumenConceptosDTO(total, activos, ingresos, gastos, inactivos);
    }

    @Override
    public Concepto cambiarEstado(Integer id) {
        Concepto concepto = buscarPorId(id);
        concepto.setActivo(!Boolean.TRUE.equals(concepto.getActivo()));
        return conceptoRepository.save(concepto);
    }

    @Override
    public boolean puedeCambiarTipo(Integer id) {
        return !tieneTransacciones(id) && !tieneGastosFijos(id);
    }

    @Override
    public List<Transaccion> obtenerUltimasTransacciones(Integer id) {
        buscarPorId(id);
        return transaccionRepository.findTop10ByConceptoIdConceptoOrderByFechaDescFechaRegistroDesc(id);
    }

    private void validarConcepto(Concepto concepto, Integer idConceptoActual) {
        if (concepto == null) {
            throw new IllegalArgumentException("El concepto es obligatorio.");
        }
        concepto.setNombre(normalizarTextoObligatorio(concepto.getNombre(), "El nombre es obligatorio."));
        if (concepto.getTipo() == null) {
            throw new IllegalArgumentException("El tipo es obligatorio.");
        }
        boolean duplicado = idConceptoActual == null
                ? conceptoRepository.existsByNombreNormalizadoAndTipo(concepto.getNombre(), concepto.getTipo())
                : conceptoRepository.existsByNombreNormalizadoAndTipoAndIdConceptoNot(
                        concepto.getNombre(),
                        concepto.getTipo(),
                        idConceptoActual);
        if (duplicado) {
            throw new IllegalArgumentException("Ya existe un concepto con ese nombre y tipo.");
        }
    }

    private void validarCambioTipo(Concepto conceptoExistente, Concepto conceptoNuevo) {
        if (conceptoExistente.getTipo() == conceptoNuevo.getTipo()) {
            return;
        }
        if (tieneTransacciones(conceptoExistente.getIdConcepto())) {
            throw new IllegalArgumentException("No puedes cambiar el tipo porque este concepto ya tiene movimientos asociados.");
        }
        if (tieneGastosFijos(conceptoExistente.getIdConcepto()) && conceptoNuevo.getTipo() == TipoConcepto.INGRESO) {
            throw new IllegalArgumentException("Los gastos fijos solo pueden usar conceptos de tipo GASTO.");
        }
    }

    private ConceptoResumenDTO convertirAResumen(Concepto concepto) {
        Long cantidadTransacciones = obtenerLong(transaccionRepository.contarPorConcepto(concepto.getIdConcepto()));
        Long cantidadGastosFijos = obtenerLong(gastoFijoRepository.contarPorConcepto(concepto.getIdConcepto()));
        return new ConceptoResumenDTO(
                concepto.getIdConcepto(),
                concepto.getNombre(),
                concepto.getTipo(),
                concepto.getActivo(),
                cantidadTransacciones,
                cantidadGastosFijos,
                cantidadTransacciones == 0 && cantidadGastosFijos == 0);
    }

    private boolean tieneTransacciones(Integer idConcepto) {
        return obtenerLong(transaccionRepository.contarPorConcepto(idConcepto)) > 0;
    }

    private boolean tieneGastosFijos(Integer idConcepto) {
        return obtenerLong(gastoFijoRepository.contarPorConcepto(idConcepto)) > 0;
    }

    private String normalizarTextoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }

    private Long obtenerLong(Long valor) {
        return valor != null ? valor : 0L;
    }
}
