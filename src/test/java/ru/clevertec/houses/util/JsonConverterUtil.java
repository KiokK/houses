package ru.clevertec.houses.util;

import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonConverterUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static Object readJsonString(String jsonString, Class<?> clazz) throws IOException {
        return mapper.readValue(jsonString, clazz);
    }
}
