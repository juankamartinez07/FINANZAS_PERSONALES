package com.finanzas.finanzaspersonales.exception;

public class TransaccionNoEncontradaException extends RuntimeException {

    public TransaccionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
