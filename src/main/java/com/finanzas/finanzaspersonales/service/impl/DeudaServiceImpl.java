package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.DeudaResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenDeudasDTO;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import com.finanzas.finanzaspersonales.entity.Deuda;
import com.finanzas.finanzaspersonales.exception.CuentaNoEncontradaException;
import com.finanzas.finanzaspersonales.exception.DeudaNoEncontradaException;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.repository.DeudaRepository;
import com.finanzas.finanzaspersonales.service.DeudaService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DeudaServiceImpl implements DeudaService {

    private static final BigDecimal CIEN = new BigDecimal("100");

    private final DeudaRepository deudaRepository;
    private final CuentaRepository cuentaRepository;

    public DeudaServiceImpl(DeudaRepository deudaRepository, CuentaRepository cuentaRepository) {
        this.deudaRepository = deudaRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<Deuda> listar() {
        return deudaRepository.findAll();
    }

    @Override
    public Deuda buscarPorId(Integer id) {
        return deudaRepository.findById(id)
                .orElseThrow(() -> new DeudaNoEncontradaException("Deuda no encontrada"));
    }

    @Override
    public Deuda guardar(Deuda deuda) {
        validarDeuda(deuda);
        deuda.setActivo(true);
        deuda.setCuenta(obtenerCuentaExistente(deuda));
        return deudaRepository.save(deuda);
    }

    @Override
    public Deuda actualizar(Integer id, Deuda deuda) {
        Deuda deudaExistente = buscarPorId(id);
        validarDeuda(deuda);
        deudaExistente.setNombre(deuda.getNombre());
        deudaExistente.setEntidad(deuda.getEntidad());
        deudaExistente.setSaldoInicial(deuda.getSaldoInicial());
        deudaExistente.setSaldoActual(deuda.getSaldoActual());
        deudaExistente.setCuotaMinima(deuda.getCuotaMinima());
        deudaExistente.setFechaInicio(deuda.getFechaInicio());
        deudaExistente.setFechaVencimiento(deuda.getFechaVencimiento());
        deudaExistente.setActivo(deuda.getActivo() != null ? deuda.getActivo() : deudaExistente.getActivo());
        deudaExistente.setCuenta(obtenerCuentaExistente(deuda));
        return deudaRepository.save(deudaExistente);
    }

    @Override
    public void eliminar(Integer id) {
        Deuda deuda = buscarPorId(id);
        deudaRepository.delete(deuda);
    }

    @Override
    public List<Deuda> listarActivas() {
        return deudaRepository.findByActivoTrue();
    }

    @Override
    public List<Deuda> listarPorCuenta(Integer idCuenta) {
        return deudaRepository.findByCuentaIdCuenta(idCuenta);
    }

    @Override
    public List<Deuda> listarPorEntidad(String entidad) {
        return deudaRepository.findByEntidad(entidad);
    }

    @Override
    public List<DeudaResumenDTO> listarResumen(String estado, Integer idCuenta, String buscar) {
        Boolean activo = obtenerFiltroActivo(estado);
        String busqueda = normalizarTextoOpcional(buscar);
        return deudaRepository.buscarConFiltros(idCuenta, activo, busqueda).stream()
                .map(this::convertirAResumen)
                .filter(deuda -> coincideEstado(deuda, estado))
                .sorted(Comparator.comparingInt(this::prioridadEstado)
                        .thenComparing(this::fechaOrdenamiento)
                        .thenComparing(DeudaResumenDTO::getSaldoActual, Comparator.reverseOrder()))
                .toList();
    }

    @Override
    public DeudaResumenDTO obtenerResumenPorId(Integer id) {
        return convertirAResumen(buscarPorId(id));
    }

    @Override
    public ResumenDeudasDTO obtenerResumenGeneral() {
        return new ResumenDeudasDTO(
                obtenerBigDecimal(deudaRepository.sumarSaldoPendienteActivo()),
                obtenerBigDecimal(deudaRepository.sumarTotalPagadoActivo()),
                obtenerLong(deudaRepository.countByActivoTrue()),
                obtenerLong(deudaRepository.contarVencidas(LocalDate.now())),
                obtenerBigDecimal(deudaRepository.sumarCuotasMinimasActivas()));
    }

    @Override
    public ResumenDeudasDTO obtenerResumenFiltrado(List<DeudaResumenDTO> deudas) {
        BigDecimal totalSaldoPendienteActivo = BigDecimal.ZERO;
        BigDecimal totalPagado = BigDecimal.ZERO;
        BigDecimal totalCuotasMinimas = BigDecimal.ZERO;
        long cantidadDeudasActivas = 0L;
        long cantidadDeudasVencidas = 0L;

        for (DeudaResumenDTO deuda : deudas) {
            if (Boolean.TRUE.equals(deuda.getActivo())) {
                cantidadDeudasActivas++;
                totalPagado = totalPagado.add(obtenerBigDecimal(deuda.getValorPagado()));
                totalCuotasMinimas = totalCuotasMinimas.add(obtenerBigDecimal(deuda.getCuotaMinima()));
                if (obtenerBigDecimal(deuda.getSaldoActual()).compareTo(BigDecimal.ZERO) > 0) {
                    totalSaldoPendienteActivo = totalSaldoPendienteActivo.add(deuda.getSaldoActual());
                }
            }
            if ("VENCIDA".equals(deuda.getEstadoVisual())) {
                cantidadDeudasVencidas++;
            }
        }

        return new ResumenDeudasDTO(
                totalSaldoPendienteActivo,
                totalPagado,
                cantidadDeudasActivas,
                cantidadDeudasVencidas,
                totalCuotasMinimas);
    }

    @Override
    public Deuda cambiarEstado(Integer id) {
        Deuda deuda = buscarPorId(id);
        deuda.setActivo(!Boolean.TRUE.equals(deuda.getActivo()));
        return deudaRepository.save(deuda);
    }

    private void validarDeuda(Deuda deuda) {
        if (deuda == null) {
            throw new IllegalArgumentException("La deuda es obligatoria.");
        }
        deuda.setNombre(normalizarTextoObligatorio(deuda.getNombre(), "El nombre es obligatorio."));
        deuda.setEntidad(normalizarTextoObligatorio(deuda.getEntidad(), "La entidad es obligatoria."));

        if (deuda.getSaldoInicial() == null || deuda.getSaldoInicial().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser mayor que cero.");
        }
        if (deuda.getSaldoActual() == null || deuda.getSaldoActual().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo actual no puede ser negativo.");
        }
        if (deuda.getSaldoActual().compareTo(deuda.getSaldoInicial()) > 0) {
            throw new IllegalArgumentException("El saldo actual no puede superar el saldo inicial.");
        }
        if (deuda.getCuotaMinima() == null || deuda.getCuotaMinima().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cuota minima no puede ser negativa.");
        }
        if (deuda.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }
        if (deuda.getFechaVencimiento() != null
                && deuda.getFechaVencimiento().isBefore(deuda.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de inicio.");
        }
        obtenerCuentaExistente(deuda);
    }

    private Cuenta obtenerCuentaExistente(Deuda deuda) {
        if (deuda.getCuenta() == null || deuda.getCuenta().getIdCuenta() == null) {
            throw new IllegalArgumentException("La cuenta asociada es obligatoria.");
        }
        return cuentaRepository.findById(deuda.getCuenta().getIdCuenta())
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    private DeudaResumenDTO convertirAResumen(Deuda deuda) {
        BigDecimal saldoInicial = obtenerBigDecimal(deuda.getSaldoInicial());
        BigDecimal saldoActual = obtenerBigDecimal(deuda.getSaldoActual());
        BigDecimal valorPagado = saldoInicial.subtract(saldoActual);
        if (valorPagado.compareTo(BigDecimal.ZERO) < 0) {
            valorPagado = BigDecimal.ZERO;
        }
        BigDecimal porcentajePagado = calcularPorcentajePagado(saldoInicial, valorPagado);
        Integer diasParaVencimiento = calcularDiasParaVencimiento(deuda.getFechaVencimiento());

        return new DeudaResumenDTO(
                deuda.getIdDeuda(),
                deuda.getNombre(),
                deuda.getEntidad(),
                saldoInicial,
                saldoActual,
                obtenerBigDecimal(deuda.getCuotaMinima()),
                deuda.getFechaInicio(),
                deuda.getFechaVencimiento(),
                deuda.getActivo(),
                deuda.getCuenta() != null ? deuda.getCuenta().getNombre() : "Sin cuenta",
                porcentajePagado,
                valorPagado,
                calcularEstadoVisual(deuda, diasParaVencimiento),
                diasParaVencimiento);
    }

    private BigDecimal calcularPorcentajePagado(BigDecimal saldoInicial, BigDecimal valorPagado) {
        if (saldoInicial.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal porcentaje = valorPagado.multiply(CIEN).divide(saldoInicial, 2, RoundingMode.HALF_UP);
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        if (porcentaje.compareTo(CIEN) > 0) {
            return CIEN;
        }
        return porcentaje;
    }

    private String calcularEstadoVisual(Deuda deuda, Integer diasParaVencimiento) {
        if (!Boolean.TRUE.equals(deuda.getActivo())) {
            return "INACTIVA";
        }
        if (obtenerBigDecimal(deuda.getSaldoActual()).compareTo(BigDecimal.ZERO) == 0) {
            return "PAGADA";
        }
        if (diasParaVencimiento != null && diasParaVencimiento < 0) {
            return "VENCIDA";
        }
        if (diasParaVencimiento != null && diasParaVencimiento <= 7) {
            return "PROXIMA A VENCER";
        }
        return "ACTIVA";
    }

    private Integer calcularDiasParaVencimiento(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) {
            return null;
        }
        return Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento));
    }

    private boolean coincideEstado(DeudaResumenDTO deuda, String estado) {
        String filtro = normalizarTextoOpcional(estado);
        return filtro == null || "TODAS".equals(filtro) || deuda.getEstadoVisual().equals(filtro);
    }

    private Boolean obtenerFiltroActivo(String estado) {
        String filtro = normalizarTextoOpcional(estado);
        if ("INACTIVA".equals(filtro)) {
            return false;
        }
        if ("ACTIVA".equals(filtro) || "PAGADA".equals(filtro) || "PROXIMA A VENCER".equals(filtro) || "VENCIDA".equals(filtro)) {
            return true;
        }
        return null;
    }

    private int prioridadEstado(DeudaResumenDTO deuda) {
        return switch (deuda.getEstadoVisual()) {
            case "VENCIDA" -> 1;
            case "PROXIMA A VENCER" -> 2;
            case "ACTIVA" -> 3;
            case "PAGADA" -> 4;
            case "INACTIVA" -> 5;
            default -> 6;
        };
    }

    private LocalDate fechaOrdenamiento(DeudaResumenDTO deuda) {
        return deuda.getFechaVencimiento() != null ? deuda.getFechaVencimiento() : LocalDate.MAX;
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
        return valor.trim().toUpperCase();
    }

    private BigDecimal obtenerBigDecimal(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private Long obtenerLong(Long valor) {
        return valor != null ? valor : 0L;
    }
}
