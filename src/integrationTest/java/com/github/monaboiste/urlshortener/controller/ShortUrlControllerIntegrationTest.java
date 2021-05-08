package com.github.monaboiste.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "server.domain=http://localhost",
                "client.domain=http://localhost",
                "client.port=8080"
        }
)
public class ShortUrlControllerIntegrationTest {

    @LocalServerPort
    private int testServerPort;

    @Value("${server.domain}")
    private String testServerUrl;

    @Value("${client.port}")
    private int testClientPort;

    @Value("${client.domain}")
    private String testClientUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testServerUrl = String.format("%s:%d", testServerUrl, testServerPort);
        testClientUrl = String.format("%s:%d", testClientUrl, testClientPort);
    }

    @Test
    void shouldCreateNewShortUrlAndReturnCreatedShortUrlDto() throws Exception {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .build();
        final String redirectingUrl = String.format("%s/%s",testClientUrl, shortUrlDto.getAlias());

        final ResponseEntity<ShortUrlDto> response = restTemplate.postForEntity(
                testServerUrl + "/api/short_urls",
                shortUrlDto,
                ShortUrlDto.class
        );
        final ShortUrlDto shortUrlDtoInResponse = response.getBody();

        assertAll(
                () -> assertEquals(response.getStatusCode(), HttpStatus.CREATED),
                () -> assertEquals(shortUrlDto.getId(), shortUrlDtoInResponse.getId()),
                () -> assertEquals(shortUrlDto.getUrl(), shortUrlDtoInResponse.getUrl()),
                () -> assertEquals(shortUrlDto.getAlias(), shortUrlDtoInResponse.getAlias()),
                () -> assertEquals(redirectingUrl, shortUrlDtoInResponse.getRedirectingUrl()),
                () -> assertNotNull(shortUrlDtoInResponse.getCreatedAt())
        );
    }
}
