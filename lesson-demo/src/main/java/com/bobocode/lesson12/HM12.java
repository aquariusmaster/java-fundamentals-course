package com.bobocode.lesson12;

import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

import static com.bobocode.HTTPGet.parseImageUrls;
import static java.lang.Long.parseLong;
import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.util.Comparator.comparingLong;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class HM12 {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        var link = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=10&api_key=DEMO_KEY";
        parseImageUrls(get(link).body())
                .parallelStream()
                .map(HM12::get)
                .filter(res -> nonNull(getHeader(res, "Location")))
                .map(res -> getHeader(res, "Location"))
                .map(HM12::head)
                .max(comparingLong(res -> parseLong(requireNonNull(getHeader(res, "Content-Length")))))
                .ifPresent(res -> System.out.printf("%s - length: %s", res.uri(), getHeader(res, "Content-Length")));
    }

    @SneakyThrows
    private static HttpResponse<String> exchange(String link, String method, HttpRequest.BodyPublisher bodyPublisher) {
        var request = HttpRequest.newBuilder(new URI(link)).method(method, bodyPublisher).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String getHeader(HttpResponse<String> response, String header) {
        List<String> values = response.headers().map().get(header);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private static HttpResponse<String> get(String link) {
        return exchange(link, "GET", noBody());
    }

    private static HttpResponse<String> head(String link) {
        return exchange(link, "HEAD", noBody());
    }
}
