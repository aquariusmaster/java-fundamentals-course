package com.bobocode.lesson12;

import com.bobocode.HTTPGet;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

import static java.util.Comparator.comparingLong;

public class HM12 {
    public static void main(String[] args) {
        List<String> urls = HTTPGet.getByHttpClient("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=10&api_key=DEMO_KEY");
        HttpResponse<String> max = urls
                .parallelStream()
                .map(url -> getResponse(url, "GET"))
                .filter(res -> Objects.nonNull(getHeader(res, "Location")))
                .map(res -> getHeader(res, "Location"))
                .map(url -> getResponse(url, "HEAD"))
                .filter(res -> Objects.nonNull(getHeader(res, "Content-Length")))
                .max(comparingLong(r -> Long.parseLong(getHeader(r, "Content-Length"))))
                .orElseThrow();
        System.out.println(max.uri());
        System.out.println(Long.parseLong(getHeader(max, "Content-Length")));

    }

    @SneakyThrows
    public static HttpResponse<String> getResponse(String link, String method) {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(new URI(link)).method(method, HttpRequest.BodyPublishers.noBody()).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String getHeader(HttpResponse<String> response, String header) {
        Objects.requireNonNull(response);
        List<String> values = response.headers().map().get(header);
        return values == null || values.isEmpty() ? null : values.get(0);
    }
}
