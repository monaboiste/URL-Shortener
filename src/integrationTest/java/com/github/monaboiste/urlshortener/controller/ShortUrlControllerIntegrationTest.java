package com.github.monaboiste.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monaboiste.urlshortener.UrlshortenerApplication;
import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShortUrlControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        baseUrl = String.format("http://localhost:%d", port);
    }

    @Test
    void shouldCreateNewShortUrl() throws Exception {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .build();

        ResponseEntity<ShortUrlDto> response = restTemplate.postForEntity(
                baseUrl + "/short_urls",
                shortUrlDto,
                ShortUrlDto.class
        );
        ShortUrlDto shortUrlDtoInResponse = response.getBody();

        assertAll(
                () -> assertEquals(response.getStatusCode(), HttpStatus.CREATED),
                () -> assertEquals(shortUrlDto.getId(), shortUrlDtoInResponse.getId()),
                () -> assertEquals(shortUrlDto.getUrl(), shortUrlDtoInResponse.getUrl()),
                () -> assertEquals(shortUrlDto.getAlias(), shortUrlDtoInResponse.getAlias()),
                () -> assertNotNull(shortUrlDtoInResponse.getCreatedAt())
        );
    }
}
