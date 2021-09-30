package com.bobocode.lesson12;

import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class CustomJackson {
    public static void main(String[] args) {
        var json = "{\n" +
                "  \"firstName\": \"Andrii\",\n" +
                "  \"lastName\": \"Shtramak\",\n" +
                "  \"email\": \"shtramak@gmail.com\",\n" +
                "  \"active\": true,\n" +
                "  \"age\": 19\n" +
                "}";
        User user = jsonToObj(json, User.class);
        System.out.println(user);
    }

    @SneakyThrows
    public static <T> T jsonToObj(String json, Class<T> clazz) {
        Map<String, String> jsonMap = json.lines().collect(
                toMap(CustomJackson::parseKey, CustomJackson::parseValue, (l, r) -> r)
        );
        T instance = clazz.getDeclaredConstructor().newInstance();
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> setValueToField(instance, field, jsonMap.get(field.getName())));

        return instance;
    }

    @SneakyThrows
    private static <T> void setValueToField(T instance, Field field, String strValue) {

        field.setAccessible(true);
        System.out.println("Type: " + field.getType());
        if (field.getType().equals(String.class)) {
            field.set(instance, strValue);
        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            field.set(instance, Boolean.parseBoolean(strValue));
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            field.set(instance, Integer.parseInt(strValue));
        } else if (field.getType() == long.class || field.getType() == Long.class) {
            field.set(instance, Long.parseLong(strValue));
        } else if (field.getType() == double.class || field.getType() == Double.class) {
            field.set(instance, Double.parseDouble(strValue));
        } else {
            System.out.println("Type is unsupported, ignoring");
        }
    }

    @Data
    static class User {
        private String firstName;
        private String lastName;
        private String email;
        private Integer age;
        private boolean active;
    }

    public static String parseKey(String line) {
        int start = line.indexOf("\"");
        if (start == -1) return null;
        int end = line.indexOf("\":", start);
        if (start + 1 > end) return null;
        return line.substring(start + 1, end);
    }

    public static String parseValue(String line) {
        int start = line.lastIndexOf(": ");
        if (start == -1) return "";
        boolean isStringValue = line.charAt(start + 2) == '"';
        if (isStringValue) {
            int end = line.lastIndexOf("\"");
            if (end == -1) return "";
            return line.substring(start + 3, end);
        }
        int end = -1;
        if (line.charAt(line.length() - 1) == ',') {
            end = line.lastIndexOf(",");
        } else {
            end = line.length();
        }
        if (end == -1 || start + 2 > end) return "";
        return line.substring(start + 2, end);
    }

}
