package com.bobocode;

import lombok.SneakyThrows;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPGet {
    @SneakyThrows
    public static void main(String[] args) {
        var link = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=DEMO_KEY";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(link)).GET().build();
        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());

        List<String> images = Arrays.stream(response.body().split("img_src"))
                .map(line->line.split("\",\"earth_date\"")[0])
                .map(line->line.substring(3))
                .filter(line -> line.startsWith("http"))
//                .peek(System.out::println)
                .collect(Collectors.toList());

        System.out.println(images);
    }
}
