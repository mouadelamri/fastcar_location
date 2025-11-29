package com.fastcar.fastcarlocation.exception;

/**
 * Exception lancée lorsqu'une entité n'est pas trouvée dans la base de données.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}

