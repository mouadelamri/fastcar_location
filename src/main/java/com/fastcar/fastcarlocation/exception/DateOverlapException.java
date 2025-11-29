package com.fastcar.fastcarlocation.exception;

/**
 * Exception lancée lorsqu'il y a un chevauchement de dates pour une location.
 */
public class DateOverlapException extends RuntimeException {
    public DateOverlapException(String message) {
        super(message);
    }
}

