package com.dalendev.finance.cryptobot.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author daniele.orler
 */
public class TestUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T jsonFileToObject(String file, Class<T> valueType) {
        String filePath = ClassLoader.getSystemClassLoader().getResource(file).getFile();
        try {
            return objectMapper.readValue(new File(filePath), valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String jsonFileToString(String file) {
        String filePath = ClassLoader.getSystemClassLoader().getResource(file).getFile();
        try {
            return new String(Files.readAllBytes(new File(filePath).toPath()));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read file " + file);
        }
    }

    public static <T> T jsonStringToObject(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            return null;
        }
    }

}
