package com.bobocode.lesson12;

import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class CustomJackson {
    public static void main(String[] args) {
        var json = "{\n" +
                "  \"firstName\": \"Andrii\",\n" +
                "  \"lastName\": \"Shtramak\",\n" +
                "  \"email\": \"shtramak@gmail.com\"\n" +
                "}";
        jsonToObj(json, User.class);
    }

    @SneakyThrows
    public static <T> T jsonToObj(String json, Class<T> clazz) {
        Map<String, List<Field>> fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.groupingBy(Field::getName));

        T t = clazz.getDeclaredConstructor().newInstance();
        Map<String, String> strings = json.lines()
                .collect(
                        toMap(
                                CustomJackson::parseKey,
                                CustomJackson::parseValue,
                                (l, r) -> r
                        )
                );

        System.out.println(strings);
        return null;
    }

    @Data
    static class User {
        private String firstName;
        private String lastName;
        private String email;
    }

    public static String parseKey(String line) {
        int start = line.indexOf("\"");
        if (start == -1) return null;
        int end = line.indexOf("\":", start);
        if (start + 1 > end) return null;
        return line.substring(start + 1, end);
    }

    public static String parseValue(String line) {
        int start = line.lastIndexOf(": \"");
        int end = line.lastIndexOf("\"");
        if (start == -1 || start + 2 > end) return "";
        return line.substring(start + 2, end);
    }
}
