package ru.clevertec.houses.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Entity by uuid is not found");
    }

    public EntityNotFoundException(UUID uuid) {
        super(String.format("Entity with uuid='%s' not found", uuid));
    }

}
