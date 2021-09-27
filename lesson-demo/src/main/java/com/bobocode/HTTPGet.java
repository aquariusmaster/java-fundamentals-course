package com.bobocode;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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
        System.out.println("--------------------HttpClient---------------------------------");
        getByHttpClient(link).forEach(System.out::println);
        System.out.println("--------------------Open URLConnection-------------------------");
        getByURLConnection(link).forEach(System.out::println);
        System.out.println("--------------------Make curl request--------------------------");
        getByCurl(link).forEach(System.out::println);
        System.out.println("---------------------------------------------------------------");
    }

    @SneakyThrows
    public static List<String> getByHttpClient(String url) {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(new URI(url)).GET().build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return extractImageUrls(response.body());
    }

    @SneakyThrows
    public static List<String> getByURLConnection(String link) {
        var connection = new URL(link).openConnection();
        var body = getInputStreamAsString(connection.getInputStream());

        return extractImageUrls(body);
    }

    @SneakyThrows
    public static List<String> getByCurl(String url) {
        var curlRequestProcess = Runtime.getRuntime().exec("curl -X GET " + url);
        var body = getInputStreamAsString(curlRequestProcess.getInputStream());
        return extractImageUrls(body);
    }

    @SneakyThrows
    private static String getInputStreamAsString(InputStream inputStream) {
        try (var in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            StringBuilder result = new StringBuilder();
            while((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    private static List<String> extractImageUrls(String body) {
        return Arrays.stream(body.split("img_src\":\""))
                .map(line -> line.substring(0, line.indexOf(",") - 1))
                .filter(line -> line.startsWith("http"))
                .collect(Collectors.toList());
    }
}
