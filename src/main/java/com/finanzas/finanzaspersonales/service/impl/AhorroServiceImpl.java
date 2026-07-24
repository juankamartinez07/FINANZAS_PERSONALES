package com.finanzas.finanzaspersonales.service.impl;

import com.finanzas.finanzaspersonales.dto.AhorroResumenDTO;
import com.finanzas.finanzaspersonales.dto.ResumenAhorrosDTO;
import com.finanzas.finanzaspersonales.entity.Ahorro;
import com.finanzas.finanzaspersonales.exception.AhorroNoEncontradoException;
import com.finanzas.finanzaspersonales.repository.AhorroRepository;
import com.finanzas.finanzaspersonales.service.AhorroService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AhorroServiceImpl implements AhorroService {

    private static final BigDecimal CIEN = new BigDecimal("100");

    private final AhorroRepository ahorroRepository;

    public AhorroServiceImpl(AhorroRepository ahorroRepository) {
        this.ahorroRepository = ahorroRepository;
    }

    @Override
    public List<Ahorro> listar() {
        return ahorroRepository.findAll();
    }

    @Override
    public Ahorro buscarPorId(Integer id) {
        return ahorroRepository.findById(id)
                .orElseThrow(() -> new AhorroNoEncontradoException("Ahorro no encontrado"));
    }

    @Override
    public Ahorro guardar(Ahorro ahorro) {
        validarAhorro(ahorro);
        ahorro.setActivo(true);
        if (ahorro.getDescripcion() != null) {
            ahorro.setDescripcion(recortarTextoOpcional(ahorro.getDescripcion()));
        }
        return ahorroRepository.save(ahorro);
    }

    @Override
    public Ahorro actualizar(Integer id, Ahorro ahorro) {
        Ahorro ahorroExistente = buscarPorId(id);
        validarAhorro(ahorro);
        ahorroExistente.setNombre(ahorro.getNombre());
        ahorroExistente.setDescripcion(recortarTextoOpcional(ahorro.getDescripcion()));
        ahorroExistente.setMeta(ahorro.getMeta());
        ahorroExistente.setAhorroActual(ahorro.getAhorroActual());
        ahorroExistente.setPorcentajeRecomendado(ahorro.getPorcentajeRecomendado());
        ahorroExistente.setActivo(ahorro.getActivo() != null ? ahorro.getActivo() : ahorroExistente.getActivo());
        return ahorroRepository.save(ahorroExistente);
    }

    @Override
    public void eliminar(Integer id) {
        Ahorro ahorro = buscarPorId(id);
        ahorroRepository.delete(ahorro);
    }

    @Override
    public List<Ahorro> listarActivos() {
        return ahorroRepository.findByActivoTrue();
    }

    @Override
    public List<AhorroResumenDTO> listarResumen(String estado, String buscar) {
        Boolean activo = obtenerFiltroActivo(estado);
        String busqueda = normalizarTextoOpcional(buscar);
        return ahorroRepository.buscarConFiltros(activo, busqueda).stream()
                .map(this::convertirAResumen)
                .filter(ahorro -> coincideEstado(ahorro, estado))
                .sorted(Comparator.comparingInt(this::prioridadEstado)
                        .thenComparing(AhorroResumenDTO::getValorFaltante)
                        .thenComparing(AhorroResumenDTO::getNombre))
                .toList();
    }

    @Override
    public AhorroResumenDTO obtenerResumenPorId(Integer id) {
        return convertirAResumen(buscarPorId(id));
    }

    @Override
    public ResumenAhorrosDTO obtenerResumenGeneral() {
        BigDecimal totalAhorrado = obtenerBigDecimal(ahorroRepository.sumarAhorradoActivo());
        BigDecimal totalObjetivos = obtenerBigDecimal(ahorroRepository.sumarObjetivosActivos());
        return new ResumenAhorrosDTO(
                totalAhorrado,
                totalObjetivos,
                obtenerBigDecimal(ahorroRepository.sumarFaltanteActivo()),
                obtenerLong(ahorroRepository.countByActivoTrue()),
                obtenerLong(ahorroRepository.contarCompletadasActivas()),
                calcularPorcentaje(totalObjetivos, totalAhorrado));
    }

    @Override
    public ResumenAhorrosDTO obtenerResumenFiltrado(List<AhorroResumenDTO> ahorros) {
        BigDecimal totalAhorrado = BigDecimal.ZERO;
        BigDecimal totalObjetivos = BigDecimal.ZERO;
        BigDecimal valorFaltante = BigDecimal.ZERO;
        long cantidadActivas = 0L;
        long cantidadCompletadas = 0L;

        for (AhorroResumenDTO ahorro : ahorros) {
            if (Boolean.TRUE.equals(ahorro.getActivo())) {
                cantidadActivas++;
                totalAhorrado = totalAhorrado.add(obtenerBigDecimal(ahorro.getValorAhorrado()));
                totalObjetivos = totalObjetivos.add(obtenerBigDecimal(ahorro.getValorObjetivo()));
                valorFaltante = valorFaltante.add(obtenerBigDecimal(ahorro.getValorFaltante()));
                if ("COMPLETADA".equals(ahorro.getEstadoVisual())) {
                    cantidadCompletadas++;
                }
            }
        }

        return new ResumenAhorrosDTO(
                totalAhorrado,
                totalObjetivos,
                valorFaltante,
                cantidadActivas,
                cantidadCompletadas,
                calcularPorcentaje(totalObjetivos, totalAhorrado));
    }

    @Override
    public Ahorro cambiarEstado(Integer id) {
        Ahorro ahorro = buscarPorId(id);
        ahorro.setActivo(!Boolean.TRUE.equals(ahorro.getActivo()));
        return ahorroRepository.save(ahorro);
    }

    private void validarAhorro(Ahorro ahorro) {
        if (ahorro == null) {
            throw new IllegalArgumentException("La meta de ahorro es obligatoria.");
        }
        ahorro.setNombre(normalizarTextoObligatorio(ahorro.getNombre(), "El nombre de la meta es obligatorio."));
        if (ahorro.getMeta() == null || ahorro.getMeta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor objetivo debe ser mayor que cero.");
        }
        if (ahorro.getAhorroActual() == null || ahorro.getAhorroActual().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor ahorrado no puede ser negativo.");
        }
        if (ahorro.getAhorroActual().compareTo(ahorro.getMeta()) > 0) {
            throw new IllegalArgumentException("El valor ahorrado no puede superar el objetivo.");
        }
        if (ahorro.getPorcentajeRecomendado() != null
                && (ahorro.getPorcentajeRecomendado() < 0 || ahorro.getPorcentajeRecomendado() > 100)) {
            throw new IllegalArgumentException("El porcentaje recomendado debe estar entre 0 y 100.");
        }
    }

    private AhorroResumenDTO convertirAResumen(Ahorro ahorro) {
        BigDecimal valorObjetivo = obtenerBigDecimal(ahorro.getMeta());
        BigDecimal valorAhorrado = obtenerBigDecimal(ahorro.getAhorroActual());
        BigDecimal valorFaltante = valorObjetivo.subtract(valorAhorrado);
        if (valorFaltante.compareTo(BigDecimal.ZERO) < 0) {
            valorFaltante = BigDecimal.ZERO;
        }
        BigDecimal porcentajeAvance = calcularPorcentaje(valorObjetivo, valorAhorrado);

        return new AhorroResumenDTO(
                ahorro.getIdAhorro(),
                ahorro.getNombre(),
                ahorro.getDescripcion(),
                valorObjetivo,
                valorAhorrado,
                valorFaltante,
                porcentajeAvance,
                null,
                null,
                ahorro.getActivo(),
                calcularEstadoVisual(ahorro),
                null);
    }

    private String calcularEstadoVisual(Ahorro ahorro) {
        if (!Boolean.TRUE.equals(ahorro.getActivo())) {
            return "INACTIVA";
        }
        if (obtenerBigDecimal(ahorro.getAhorroActual()).compareTo(obtenerBigDecimal(ahorro.getMeta())) == 0) {
            return "COMPLETADA";
        }
        if (obtenerBigDecimal(ahorro.getAhorroActual()).compareTo(BigDecimal.ZERO) > 0) {
            return "EN PROGRESO";
        }
        return "SIN INICIAR";
    }

    private boolean coincideEstado(AhorroResumenDTO ahorro, String estado) {
        String filtro = normalizarTextoOpcional(estado);
        return filtro == null
                || "TODAS".equals(filtro)
                || ("ACTIVA".equals(filtro) && Boolean.TRUE.equals(ahorro.getActivo()))
                || ahorro.getEstadoVisual().equals(filtro);
    }

    private Boolean obtenerFiltroActivo(String estado) {
        String filtro = normalizarTextoOpcional(estado);
        if ("INACTIVA".equals(filtro)) {
            return false;
        }
        if ("ACTIVA".equals(filtro)
                || "COMPLETADA".equals(filtro)
                || "EN PROGRESO".equals(filtro)
                || "SIN INICIAR".equals(filtro)
                || "PROXIMA A VENCER".equals(filtro)
                || "VENCIDA".equals(filtro)) {
            return true;
        }
        return null;
    }

    private int prioridadEstado(AhorroResumenDTO ahorro) {
        return switch (ahorro.getEstadoVisual()) {
            case "PROXIMA A VENCER" -> 1;
            case "VENCIDA" -> 2;
            case "EN PROGRESO" -> 3;
            case "SIN INICIAR" -> 4;
            case "COMPLETADA" -> 5;
            case "INACTIVA" -> 6;
            default -> 7;
        };
    }

    private BigDecimal calcularPorcentaje(BigDecimal valorObjetivo, BigDecimal valorAhorrado) {
        if (valorObjetivo == null || valorObjetivo.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal porcentaje = obtenerBigDecimal(valorAhorrado)
                .multiply(CIEN)
                .divide(valorObjetivo, 2, RoundingMode.HALF_UP);
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        if (porcentaje.compareTo(CIEN) > 0) {
            return CIEN;
        }
        return porcentaje;
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

    private String recortarTextoOpcional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }

    private BigDecimal obtenerBigDecimal(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private Long obtenerLong(Long valor) {
        return valor != null ? valor : 0L;
    }
}
