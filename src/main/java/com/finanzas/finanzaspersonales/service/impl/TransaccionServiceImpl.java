package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.ResumenTransaccionesDTO;
import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.exception.ConceptoNoEncontradoException;
import com.finanzas.finanzaspersonales.exception.CuentaNoEncontradaException;
import com.finanzas.finanzaspersonales.exception.TransaccionNoEncontradaException;
import com.finanzas.finanzaspersonales.repository.ConceptoRepository;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.repository.TransaccionRepository;
import com.finanzas.finanzaspersonales.service.TransaccionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;
    private final ConceptoRepository conceptoRepository;

    public TransaccionServiceImpl(
            TransaccionRepository transaccionRepository,
            CuentaRepository cuentaRepository,
            ConceptoRepository conceptoRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
        this.conceptoRepository = conceptoRepository;
    }

    @Override
    public List<Transaccion> listar() {
        return transaccionRepository.findAll();
    }

    @Override
    public Transaccion buscarPorId(Integer id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new TransaccionNoEncontradaException("Transaccion no encontrada"));
    }

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        validarTransaccion(transaccion);
        transaccion.setCuenta(obtenerCuenta(transaccion));
        transaccion.setConcepto(obtenerConcepto(transaccion));
        transaccion.setObservacion(normalizarObservacion(transaccion.getObservacion()));
        transaccion.setFechaRegistro(LocalDateTime.now());
        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion actualizar(Integer id, Transaccion transaccion) {
        Transaccion transaccionExistente = buscarPorId(id);
        validarTransaccion(transaccion);
        transaccionExistente.setFecha(transaccion.getFecha());
        transaccionExistente.setValor(transaccion.getValor());
        transaccionExistente.setObservacion(normalizarObservacion(transaccion.getObservacion()));
        transaccionExistente.setCuenta(obtenerCuenta(transaccion));
        transaccionExistente.setConcepto(obtenerConcepto(transaccion));
        return transaccionRepository.save(transaccionExistente);
    }

    @Override
    public void eliminar(Integer id) {
        Transaccion transaccion = buscarPorId(id);
        transaccionRepository.delete(transaccion);
    }

    @Override
    public List<Transaccion> listarPorCuenta(Integer idCuenta) {
        return transaccionRepository.findByCuentaIdCuenta(idCuenta);
    }

    @Override
    public List<Transaccion> listarPorConcepto(Integer idConcepto) {
        return transaccionRepository.findByConceptoIdConcepto(idConcepto);
    }

    @Override
    public List<Transaccion> listarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return transaccionRepository.findByFechaBetween(inicio, fin);
    }

    @Override
    public List<Transaccion> listarConFiltros(
            TipoConcepto tipo,
            Integer idCuenta,
            Integer idConcepto,
            LocalDate desde,
            LocalDate hasta,
            String buscar) {
        return transaccionRepository.buscarConFiltros(
                tipo,
                idCuenta,
                idConcepto,
                desde,
                hasta,
                normalizarBusqueda(buscar));
    }

    @Override
    public ResumenTransaccionesDTO obtenerResumenConFiltros(
            TipoConcepto tipo,
            Integer idCuenta,
            Integer idConcepto,
            LocalDate desde,
            LocalDate hasta,
            String buscar) {
        String busqueda = normalizarBusqueda(buscar);
        BigDecimal ingresos = obtenerTotalPorTipo(TipoConcepto.INGRESO, tipo, idCuenta, idConcepto, desde, hasta, busqueda);
        BigDecimal gastos = obtenerTotalPorTipo(TipoConcepto.GASTO, tipo, idCuenta, idConcepto, desde, hasta, busqueda);
        Long cantidad = transaccionRepository.contarConFiltros(tipo, idCuenta, idConcepto, desde, hasta, busqueda);
        return new ResumenTransaccionesDTO(ingresos, gastos, ingresos.subtract(gastos), cantidad != null ? cantidad : 0L);
    }

    private void validarTransaccion(Transaccion transaccion) {
        if (transaccion == null) {
            throw new IllegalArgumentException("La transaccion es obligatoria.");
        }
        if (transaccion.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (transaccion.getValor() == null) {
            throw new IllegalArgumentException("El valor es obligatorio.");
        }
        if (transaccion.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor que cero.");
        }
        if (transaccion.getCuenta() == null || transaccion.getCuenta().getIdCuenta() == null) {
            throw new IllegalArgumentException("La cuenta es obligatoria.");
        }
        if (transaccion.getConcepto() == null || transaccion.getConcepto().getIdConcepto() == null) {
            throw new IllegalArgumentException("El concepto es obligatorio.");
        }
    }

    private Cuenta obtenerCuenta(Transaccion transaccion) {
        return cuentaRepository.findById(transaccion.getCuenta().getIdCuenta())
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    private Concepto obtenerConcepto(Transaccion transaccion) {
        return conceptoRepository.findById(transaccion.getConcepto().getIdConcepto())
                .orElseThrow(() -> new ConceptoNoEncontradoException("Concepto no encontrado"));
    }

    private String normalizarObservacion(String observacion) {
        if (observacion == null || observacion.trim().isEmpty()) {
            return null;
        }
        return observacion.trim();
    }

    private String normalizarBusqueda(String buscar) {
        if (buscar == null || buscar.trim().isEmpty()) {
            return null;
        }
        return buscar.trim();
    }

    private BigDecimal obtenerTotalPorTipo(
            TipoConcepto tipoResumen,
            TipoConcepto tipo,
            Integer idCuenta,
            Integer idConcepto,
            LocalDate desde,
            LocalDate hasta,
            String buscar) {
        BigDecimal total = transaccionRepository.sumarPorTipoConFiltros(
                tipoResumen,
                tipo,
                idCuenta,
                idConcepto,
                desde,
                hasta,
                buscar);
        return total != null ? total : BigDecimal.ZERO;
    }
}
