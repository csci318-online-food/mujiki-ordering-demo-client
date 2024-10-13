package com.csci318.client.demo.Functionality;

import java.io.Console;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

import com.csci318.client.demo.Constants.API;
import com.csci318.client.demo.Constants.Service;
import com.csci318.client.demo.DTOs.BasicDTOResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.micrometer.common.lang.Nullable;

public abstract class MujikiDemonstrator {
    private final Console console = System.console();
    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .build();

    private final String seperator;

    protected MujikiDemonstrator() {
        char[] seperatorChars = new char[64];
        Arrays.fill(seperatorChars, '=');
        seperator = new String(seperatorChars);
    }

    public abstract void run();

    @Nullable
    protected <T> UUID get(
        Service service,
        @Nullable String[] path,
        @Nullable Map<String, Object> query
    ) {
        var request = handleRequest(HttpMethod.GET, service, path, query, null);
        return handleResponse(request.exchange(this::exchange));
    }

    @Nullable
    protected <T> UUID post(
        Service service,
        @Nullable String[] path,
        @Nullable Map<String, Object> query,
        @Nullable T body
    ) {
        var request = handleRequest(HttpMethod.POST, service, path, query, body);

        if (body != null) {
            request = request.body(body);
        }

        return handleResponse(request.exchange(this::exchange));
    }

    @Nullable
    protected <T> UUID put(
        Service service,
        @Nullable String[] path,
        @Nullable Map<String, Object> query,
        @Nullable T body
    ) {
        var request = handleRequest(HttpMethod.PUT, service, path, query, body);

        if (body != null) {
            request = request.body(body);
        }

        return handleResponse(request.exchange(this::exchange));
    }

    protected void printHeader(String content) {
        console.printf("%s\n", seperator);
        console.printf("%s\n", content);
        console.printf("%s\n", seperator);
    }

    private <T> RequestBodySpec handleRequest(
        HttpMethod method,
        Service service,
        @Nullable String[] path,
        @Nullable Map<String, Object> query,
        @Nullable T body
    ) {
        String url = getApiUrl(service, path, query);
        console.printf("URL:\n");
        console.printf("%s %s\n", method.toString(), url);

        if (body != null) {
            try {
                console.printf("BODY:\n");
                console.printf("%s\n",
                    objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(body)
                );
            } catch (Throwable e) {
                console.printf("%s\n", e.toString());
            }
        }

        return restClient.method(method)
            .uri(URI.create(url));
    }

    private UUID handleResponse(@Nullable String response) {
        if (response != null) {
            console.printf("RESPONSE:\n");
            console.printf("%s\n", response);

            try {
                BasicDTOResponse dto = objectMapper.readValue(
                    response,
                    BasicDTOResponse.class
                );

                return dto.getId();
            } catch (Throwable e) {
                try {
                    BasicDTOResponse[] dtoList = objectMapper.readValue(
                        response,
                        BasicDTOResponse[].class
                    );

                    return dtoList[0].getId();
                } catch (Throwable e1) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    private String exchange(HttpRequest request, ConvertibleClientHttpResponse response) {
        try {
            Object json = objectMapper.readValue(response.getBody(), Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Throwable t) {
            return null;
        }
    }

    private String getApiUrl(
        Service service,
        @Nullable String[] path,
        @Nullable Map<String, Object> query
    ) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(API.SERVER).append(":").append(service.Port)
            .append(API.API_BASE).append(service.Base);

        if (path != null) {
            path = Arrays.stream(path).toList().toArray(path);
            resultBuilder.append("/" + String.join("/", path));
        }

        if (query != null) {
            resultBuilder.append("?");
            query.forEach((key, value) -> {
                resultBuilder.append(key);
                resultBuilder.append("=");
                if (value != null) {
                    resultBuilder.append(
                        URLEncoder.encode(value.toString(), Charset.defaultCharset())
                    );
                }
                resultBuilder.append("&");
            });
            // Remove trailing ampersand.
            resultBuilder.deleteCharAt(resultBuilder.length() - 1);
        }

        return resultBuilder.toString();
    }
}
