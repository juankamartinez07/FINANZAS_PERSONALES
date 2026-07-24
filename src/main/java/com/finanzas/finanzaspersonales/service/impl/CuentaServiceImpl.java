package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.CuentaDetalleDTO;
import com.finanzas.finanzaspersonales.dto.CuentaResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenCuentasDTO;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import com.finanzas.finanzaspersonales.entity.Transaccion;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.exception.CuentaNoEncontradaException;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.repository.TransaccionRepository;
import com.finanzas.finanzaspersonales.service.CuentaService;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CuentaServiceImpl implements CuentaService {

    private static final int LONGITUD_MAXIMA_NOMBRE = 100;

    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public List<CuentaResumenDTO> listarResumen(String buscar, Boolean activo) {
        return cuentaRepository.buscarPorNombreYEstado(normalizarTextoOpcional(buscar), activo)
                .stream()
                .map(this::crearResumen)
                .toList();
    }

    @Override
    public ResumenCuentasDTO obtenerResumenGeneral(List<CuentaResumenDTO> cuentas) {
        BigDecimal saldoTotal = cuentas.stream()
                .map(CuentaResumenDTO::getSaldoCalculado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Long cantidadActivas = cuentaRepository.countByActivoTrue();
        Long cantidadTotal = cuentaRepository.count();
        String cuentaMayorSaldo = cuentas.stream()
                .max(Comparator.comparing(CuentaResumenDTO::getSaldoCalculado))
                .map(CuentaResumenDTO::getNombre)
                .orElse(null);
        return new ResumenCuentasDTO(saldoTotal, cantidadActivas, cantidadTotal, cuentaMayorSaldo);
    }

    @Override
    public Cuenta buscarPorId(Integer id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    @Override
    public CuentaDetalleDTO obtenerDetalle(Integer id) {
        Cuenta cuenta = buscarPorId(id);
        CuentaResumenDTO resumen = crearResumen(cuenta);
        BigDecimal ingresos = obtenerTotalPorTipo(id, TipoConcepto.INGRESO);
        BigDecimal gastos = obtenerTotalPorTipo(id, TipoConcepto.GASTO);
        List<Transaccion> ultimasTransacciones =
                transaccionRepository.findTop10ByCuentaIdCuentaOrderByFechaDescFechaRegistroDesc(id);
        return new CuentaDetalleDTO(resumen, ingresos, gastos, ultimasTransacciones);
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        validarCuenta(cuenta, null);
        cuenta.setNombre(cuenta.getNombre().trim());
        cuenta.setActivo(true);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta actualizar(Integer id, Cuenta cuenta) {
        Cuenta cuentaExistente = buscarPorId(id);
        validarCuenta(cuenta, id);
        cuentaExistente.setNombre(cuenta.getNombre().trim());
        cuentaExistente.setActivo(cuenta.getActivo() != null ? cuenta.getActivo() : cuentaExistente.getActivo());
        return cuentaRepository.save(cuentaExistente);
    }

    @Override
    public Cuenta cambiarEstado(Integer id) {
        Cuenta cuenta = buscarPorId(id);
        cuenta.setActivo(!Boolean.TRUE.equals(cuenta.getActivo()));
        return cuentaRepository.save(cuenta);
    }

    private CuentaResumenDTO crearResumen(Cuenta cuenta) {
        BigDecimal ingresos = obtenerTotalPorTipo(cuenta.getIdCuenta(), TipoConcepto.INGRESO);
        BigDecimal gastos = obtenerTotalPorTipo(cuenta.getIdCuenta(), TipoConcepto.GASTO);
        Long cantidadTransacciones = transaccionRepository.contarPorCuenta(cuenta.getIdCuenta());
        return new CuentaResumenDTO(
                cuenta.getIdCuenta(),
                cuenta.getNombre(),
                cuenta.getActivo(),
                ingresos.subtract(gastos),
                cantidadTransacciones != null ? cantidadTransacciones : 0L);
    }

    private BigDecimal obtenerTotalPorTipo(Integer idCuenta, TipoConcepto tipoConcepto) {
        BigDecimal total = transaccionRepository.sumarPorCuentaYTipo(idCuenta, tipoConcepto);
        return total != null ? total : BigDecimal.ZERO;
    }

    private void validarCuenta(Cuenta cuenta, Integer idCuentaActual) {
        if (cuenta == null) {
            throw new IllegalArgumentException("No es posible completar la operacion.");
        }
        String nombre = cuenta.getNombre();
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la cuenta es obligatorio.");
        }
        if (nombre.trim().length() > LONGITUD_MAXIMA_NOMBRE) {
            throw new IllegalArgumentException("El nombre de la cuenta no puede superar 100 caracteres.");
        }
        boolean duplicada = idCuentaActual == null
                ? cuentaRepository.existsByNombreIgnoreCase(nombre.trim())
                : cuentaRepository.existsByNombreIgnoreCaseAndIdCuentaNot(nombre.trim(), idCuentaActual);
        if (duplicada) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese nombre.");
        }
    }

    private String normalizarTextoOpcional(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        return texto.trim();
    }
}
