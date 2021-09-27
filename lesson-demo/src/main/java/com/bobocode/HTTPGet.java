package com.bobocode;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPGet {
    @SneakyThrows
    public static void main(String[] args) {
        var link = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=DEMO_KEY";
        getByHttpClient(link).forEach(System.out::println);
        getByURLConnection(link).forEach(System.out::println);
    }

    @SneakyThrows
    public static List<String> getByHttpClient(String url) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return extractImageUrls(response.body());
    }

    @SneakyThrows
    public static List<String> getByURLConnection(String url) {

        try (var reader = new BufferedReader(
                new InputStreamReader(new URL(url).openConnection().getInputStream(), StandardCharsets.UTF_8))) {
            return extractImageUrls(reader.readLine());
        } catch (Exception e) {
            throw e;
        }
    }

    private static List<String> extractImageUrls(String body) {
        return Arrays.stream(body.split("img_src\":\""))
                .map(line -> line.substring(0, line.indexOf(",") - 1))
                .filter(line -> line.startsWith("http"))
                .collect(Collectors.toList());
    }
}
