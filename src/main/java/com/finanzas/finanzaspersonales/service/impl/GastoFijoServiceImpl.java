package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.GastoFijoResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenGastosFijosDTO;
import com.finanzas.finanzaspersonales.entity.Concepto;
import com.finanzas.finanzaspersonales.entity.Cuenta;
import com.finanzas.finanzaspersonales.entity.GastoFijo;
import com.finanzas.finanzaspersonales.entity.enums.TipoConcepto;
import com.finanzas.finanzaspersonales.exception.ConceptoNoEncontradoException;
import com.finanzas.finanzaspersonales.exception.CuentaNoEncontradaException;
import com.finanzas.finanzaspersonales.exception.GastoFijoNoEncontradoException;
import com.finanzas.finanzaspersonales.repository.ConceptoRepository;
import com.finanzas.finanzaspersonales.repository.CuentaRepository;
import com.finanzas.finanzaspersonales.repository.GastoFijoRepository;
import com.finanzas.finanzaspersonales.service.GastoFijoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GastoFijoServiceImpl implements GastoFijoService {

    private final GastoFijoRepository gastoFijoRepository;
    private final CuentaRepository cuentaRepository;
    private final ConceptoRepository conceptoRepository;

    public GastoFijoServiceImpl(
            GastoFijoRepository gastoFijoRepository,
            CuentaRepository cuentaRepository,
            ConceptoRepository conceptoRepository) {
        this.gastoFijoRepository = gastoFijoRepository;
        this.cuentaRepository = cuentaRepository;
        this.conceptoRepository = conceptoRepository;
    }

    @Override
    public List<GastoFijo> listar() {
        return gastoFijoRepository.findAll();
    }

    @Override
    public GastoFijo buscarPorId(Integer id) {
        return gastoFijoRepository.findById(id)
                .orElseThrow(() -> new GastoFijoNoEncontradoException("Gasto fijo no encontrado"));
    }

    @Override
    public GastoFijo guardar(GastoFijo gastoFijo) {
        validarGastoFijo(gastoFijo);
        gastoFijo.setActivo(true);
        gastoFijo.setCuenta(obtenerCuentaExistente(gastoFijo));
        gastoFijo.setConcepto(obtenerConceptoGastoExistente(gastoFijo));
        return gastoFijoRepository.save(gastoFijo);
    }

    @Override
    public GastoFijo actualizar(Integer id, GastoFijo gastoFijo) {
        GastoFijo gastoFijoExistente = buscarPorId(id);
        validarGastoFijo(gastoFijo);
        gastoFijoExistente.setNombre(gastoFijo.getNombre());
        gastoFijoExistente.setValor(gastoFijo.getValor());
        gastoFijoExistente.setDiaPago(gastoFijo.getDiaPago());
        gastoFijoExistente.setActivo(gastoFijo.getActivo() != null ? gastoFijo.getActivo() : gastoFijoExistente.getActivo());
        gastoFijoExistente.setCuenta(obtenerCuentaExistente(gastoFijo));
        gastoFijoExistente.setConcepto(obtenerConceptoGastoExistente(gastoFijo));
        return gastoFijoRepository.save(gastoFijoExistente);
    }

    @Override
    public void eliminar(Integer id) {
        GastoFijo gastoFijo = buscarPorId(id);
        gastoFijoRepository.delete(gastoFijo);
    }

    @Override
    public List<GastoFijo> listarActivos() {
        return gastoFijoRepository.findByActivoTrue();
    }

    @Override
    public List<GastoFijo> listarPorCuenta(Integer idCuenta) {
        return gastoFijoRepository.findByCuentaIdCuenta(idCuenta);
    }

    @Override
    public List<GastoFijo> listarPorDiaPago(Integer diaPago) {
        return gastoFijoRepository.findByDiaPago(diaPago);
    }

    @Override
    public List<GastoFijoResumenDTO> listarResumen(String estado, Integer idCuenta, Integer idConcepto, String buscar) {
        Boolean activo = obtenerFiltroActivo(estado);
        String busqueda = normalizarTextoOpcional(buscar);
        return gastoFijoRepository.buscarConFiltros(idCuenta, idConcepto, activo, busqueda).stream()
                .map(this::convertirAResumen)
                .filter(gastoFijo -> coincideEstado(gastoFijo, estado))
                .sorted(Comparator.comparingInt(this::prioridadEstado)
                        .thenComparing(GastoFijoResumenDTO::getProximaFechaPago)
                        .thenComparing(GastoFijoResumenDTO::getNombre))
                .toList();
    }

    @Override
    public GastoFijoResumenDTO obtenerResumenPorId(Integer id) {
        return convertirAResumen(buscarPorId(id));
    }

    @Override
    public ResumenGastosFijosDTO obtenerResumenGeneral() {
        ResumenGastosFijosDTO resumenProximos = obtenerResumenFiltrado(listarResumen("ACTIVO", null, null, null));
        return new ResumenGastosFijosDTO(
                obtenerBigDecimal(gastoFijoRepository.sumarValorActivo()),
                obtenerLong(gastoFijoRepository.countByActivoTrue()),
                resumenProximos.getPagosProximos(),
                resumenProximos.getValorProximoPagar(),
                gastoFijoRepository.findFirstByActivoTrueOrderByValorDesc()
                        .map(GastoFijo::getNombre)
                        .orElse("Sin gastos activos"));
    }

    @Override
    public ResumenGastosFijosDTO obtenerResumenFiltrado(List<GastoFijoResumenDTO> gastosFijos) {
        BigDecimal totalMensual = BigDecimal.ZERO;
        BigDecimal valorProximo = BigDecimal.ZERO;
        long cantidadActivos = 0L;
        long pagosProximos = 0L;
        GastoFijoResumenDTO mayorValor = null;

        for (GastoFijoResumenDTO gastoFijo : gastosFijos) {
            if (Boolean.TRUE.equals(gastoFijo.getActivo())) {
                cantidadActivos++;
                totalMensual = totalMensual.add(obtenerBigDecimal(gastoFijo.getValor()));
                if (gastoFijo.getDiasParaPago() != null && gastoFijo.getDiasParaPago() >= 0 && gastoFijo.getDiasParaPago() <= 7) {
                    pagosProximos++;
                    valorProximo = valorProximo.add(obtenerBigDecimal(gastoFijo.getValor()));
                }
                if (mayorValor == null || obtenerBigDecimal(gastoFijo.getValor()).compareTo(obtenerBigDecimal(mayorValor.getValor())) > 0) {
                    mayorValor = gastoFijo;
                }
            }
        }

        return new ResumenGastosFijosDTO(
                totalMensual,
                cantidadActivos,
                pagosProximos,
                valorProximo,
                mayorValor != null ? mayorValor.getNombre() : "Sin gastos activos");
    }

    @Override
    public GastoFijo cambiarEstado(Integer id) {
        GastoFijo gastoFijo = buscarPorId(id);
        gastoFijo.setActivo(!Boolean.TRUE.equals(gastoFijo.getActivo()));
        return gastoFijoRepository.save(gastoFijo);
    }

    private void validarGastoFijo(GastoFijo gastoFijo) {
        if (gastoFijo == null) {
            throw new IllegalArgumentException("El gasto fijo es obligatorio.");
        }
        gastoFijo.setNombre(normalizarTextoObligatorio(gastoFijo.getNombre(), "El nombre es obligatorio."));
        if (gastoFijo.getValor() == null || gastoFijo.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor que cero.");
        }
        if (gastoFijo.getDiaPago() == null || gastoFijo.getDiaPago() < 1 || gastoFijo.getDiaPago() > 31) {
            throw new IllegalArgumentException("El dia de pago debe estar entre 1 y 31.");
        }
        obtenerCuentaExistente(gastoFijo);
        obtenerConceptoGastoExistente(gastoFijo);
    }

    private Cuenta obtenerCuentaExistente(GastoFijo gastoFijo) {
        if (gastoFijo.getCuenta() == null || gastoFijo.getCuenta().getIdCuenta() == null) {
            throw new IllegalArgumentException("La cuenta asociada es obligatoria.");
        }
        return cuentaRepository.findById(gastoFijo.getCuenta().getIdCuenta())
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    private Concepto obtenerConceptoGastoExistente(GastoFijo gastoFijo) {
        if (gastoFijo.getConcepto() == null || gastoFijo.getConcepto().getIdConcepto() == null) {
            throw new IllegalArgumentException("El concepto asociado es obligatorio.");
        }
        Concepto concepto = conceptoRepository.findById(gastoFijo.getConcepto().getIdConcepto())
                .orElseThrow(() -> new ConceptoNoEncontradoException("Concepto no encontrado"));
        if (concepto.getTipo() != TipoConcepto.GASTO) {
            throw new IllegalArgumentException("El concepto seleccionado debe ser de tipo GASTO.");
        }
        return concepto;
    }

    private GastoFijoResumenDTO convertirAResumen(GastoFijo gastoFijo) {
        LocalDate proximaFechaPago = calcularProximaFechaPago(gastoFijo.getDiaPago(), LocalDate.now());
        Integer diasParaPago = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), proximaFechaPago));
        return new GastoFijoResumenDTO(
                gastoFijo.getIdGastoFijo(),
                gastoFijo.getNombre(),
                obtenerBigDecimal(gastoFijo.getValor()),
                gastoFijo.getDiaPago(),
                null,
                gastoFijo.getActivo(),
                gastoFijo.getCuenta() != null ? gastoFijo.getCuenta().getNombre() : "Sin cuenta",
                gastoFijo.getConcepto() != null ? gastoFijo.getConcepto().getNombre() : "Sin concepto",
                proximaFechaPago,
                diasParaPago,
                calcularEstadoVisual(gastoFijo, diasParaPago));
    }

    private LocalDate calcularProximaFechaPago(Integer diaPago, LocalDate fechaReferencia) {
        YearMonth mesActual = YearMonth.from(fechaReferencia);
        LocalDate fechaMesActual = fechaConDiaValido(mesActual, diaPago);
        if (!fechaMesActual.isBefore(fechaReferencia)) {
            return fechaMesActual;
        }
        return fechaConDiaValido(mesActual.plusMonths(1), diaPago);
    }

    private LocalDate fechaConDiaValido(YearMonth mes, Integer diaPago) {
        int diaValido = Math.min(diaPago, mes.lengthOfMonth());
        return mes.atDay(diaValido);
    }

    private String calcularEstadoVisual(GastoFijo gastoFijo, Integer diasParaPago) {
        if (!Boolean.TRUE.equals(gastoFijo.getActivo())) {
            return "INACTIVO";
        }
        if (diasParaPago == 0) {
            return "VENCE HOY";
        }
        if (diasParaPago > 0 && diasParaPago <= 7) {
            return "PROXIMO";
        }
        return "PENDIENTE";
    }

    private boolean coincideEstado(GastoFijoResumenDTO gastoFijo, String estado) {
        String filtro = normalizarTextoOpcional(estado);
        return filtro == null
                || "TODOS".equals(filtro)
                || ("ACTIVO".equals(filtro) && Boolean.TRUE.equals(gastoFijo.getActivo()))
                || ("PROXIMOS".equals(filtro) && "PROXIMO".equals(gastoFijo.getEstadoVisual()))
                || gastoFijo.getEstadoVisual().equals(filtro);
    }

    private Boolean obtenerFiltroActivo(String estado) {
        String filtro = normalizarTextoOpcional(estado);
        if ("INACTIVO".equals(filtro)) {
            return false;
        }
        if ("ACTIVO".equals(filtro) || "VENCE HOY".equals(filtro) || "PROXIMO".equals(filtro) || "PROXIMOS".equals(filtro)) {
            return true;
        }
        return null;
    }

    private int prioridadEstado(GastoFijoResumenDTO gastoFijo) {
        return switch (gastoFijo.getEstadoVisual()) {
            case "VENCE HOY" -> 1;
            case "PROXIMO" -> 2;
            case "PENDIENTE" -> 3;
            case "INACTIVO" -> 4;
            default -> 5;
        };
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
