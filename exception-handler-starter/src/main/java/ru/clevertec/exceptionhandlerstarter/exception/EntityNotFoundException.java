package ru.clevertec.exceptionhandlerstarter.exception;

import java.util.UUID;

/**
 * Exception to be thrown when entity not found.
 */
public class EntityNotFoundException extends RuntimeException {

    public static final String MESSAGE_WITH_UUID = "Entity with uuid='%s' not found";
    public static final String MESSAGE_DEFAULT = "Entity by uuid is not found";

    public EntityNotFoundException() {
        super(MESSAGE_DEFAULT);
    }

    public EntityNotFoundException(UUID uuid) {
        super(String.format(MESSAGE_WITH_UUID, uuid));
    }

}
