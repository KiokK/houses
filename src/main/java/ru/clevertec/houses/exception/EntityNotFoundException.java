package ru.clevertec.houses.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(UUID uuid) {
        super(String.format("Entity with uuid='%s' not found", uuid));
    }

}
