package com.bobocode.lesson12;

import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

import static com.bobocode.HTTPGet.parseImageUrls;
import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.util.Comparator.comparingLong;

public class HM12 {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        var link = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=10&api_key=DEMO_KEY";
        HttpResponse<String> max =
                parseImageUrls(get(link).body())
                .parallelStream()
                .map(HM12::get)
                .filter(res -> Objects.nonNull(getHeader(res, "Location")))
                .map(res -> getHeader(res, "Location"))
                .map(HM12::head)
                .filter(res -> Objects.nonNull(getHeader(res, "Content-Length")))
                .max(comparingLong(res -> Long.parseLong(getHeader(res, "Content-Length"))))
                .orElseThrow();
        System.out.println(max.uri());
        System.out.println(Long.parseLong(getHeader(max, "Content-Length")));
    }

    @SneakyThrows
    public static HttpResponse<String> get(String link) {
        return exchange(link, "GET", noBody());
    }

    @SneakyThrows
    public static HttpResponse<String> head(String link) {
        return exchange(link, "HEAD", noBody());
    }

    @SneakyThrows
    public static HttpResponse<String> exchange(String link, String method, HttpRequest.BodyPublisher bodyPublisher) {
        var request = HttpRequest.newBuilder(new URI(link)).method(method, bodyPublisher).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String getHeader(HttpResponse<String> response, String header) {
        Objects.requireNonNull(response);
        List<String> values = response.headers().map().get(header);
        return values == null || values.isEmpty() ? null : values.get(0);
    }
}
