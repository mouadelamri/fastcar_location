package com.fastcar.fastcarlocation.exception;

/**
 * Exception lancée lorsqu'un véhicule n'est pas disponible pour la location.
 */
public class VehiculeNotAvailableException extends RuntimeException {
    public VehiculeNotAvailableException(String message) {
        super(message);
    }
}

