package com.bobocode;

import lombok.SneakyThrows;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
        System.out.println("--------------------HttpClient---------------------------------");
        var httpClientResponseList = getByHttpClient(link);
        httpClientResponseList.forEach(System.out::println);
        System.out.println("Size: " + httpClientResponseList.size());
        System.out.println("--------------------Open URLConnection-------------------------");
        var uRLConnectionResponseList = getByURLConnection(link);
        uRLConnectionResponseList.forEach(System.out::println);
        System.out.println("Size: " + uRLConnectionResponseList.size());
        System.out.println("--------------------Make curl request--------------------------");
        var curlResponseList = getByCurl(link);
        curlResponseList.forEach(System.out::println);
        System.out.println("Size: " + curlResponseList.size());
        System.out.println("--------------------Socket-------------------------------------");
        var socketResponseList = getBySocket(link);
        socketResponseList.forEach(System.out::println);
        System.out.println("Size: " + socketResponseList.size());
        System.out.println("---------------------------------------------------------------");
    }

    @SneakyThrows
    public static List<String> getByHttpClient(String link) {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(new URI(link)).GET().build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return extractImageUrls(response.body());
    }

    @SneakyThrows
    public static List<String> getByURLConnection(String link) {
        var connection = new URL(link).openConnection();
        var responseBody = readToString(connection.getInputStream());

        return extractImageUrls(responseBody);
    }

    @SneakyThrows
    public static List<String> getByCurl(String link) {
        var curlRequestProcess = Runtime.getRuntime().exec("curl " + link);
        var responseBody = readToString(curlRequestProcess.getInputStream());
        return extractImageUrls(responseBody);
    }

    @SneakyThrows
    public static List<String> getBySocket(String link) {
        var url = new URL(link);
        var factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        var socket = (SSLSocket) factory.createSocket(url.getHost(), 443);
        var out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

        out.println("GET " + url.getPath() + "?" + url.getQuery() + " HTTP/1.0");
        out.println("Host: " + url.getHost());
        out.println();
        out.flush();

        var body = readToString(socket.getInputStream());
        socket.close();
        return extractImageUrls(body);
    }

    @SneakyThrows
    private static String readToString(InputStream inputStream) {
        try (var in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    private static List<String> extractImageUrls(String responseBody) {
        return Arrays.stream(responseBody.split("img_src\":\""))
                .map(line -> line.substring(0, line.indexOf("\",")))
                .filter(line -> line.startsWith("http"))
                .collect(Collectors.toList());
    }
}
