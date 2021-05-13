package com.github.monaboiste.urlshortener.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.error.Error;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @Autowired
    private ObjectMapper objectMapper;

    private TestRestTemplate restTemplate;

    @BeforeAll
    void setUp() {
        testServerUrl = String.format("%s:%d", testServerUrl, testServerPort);
        testClientUrl = String.format("%s:%d", testClientUrl, testClientPort);
        restTemplate = new TestRestTemplate();
    }

    @Test
    @Order(1)
    void shouldCreateNewShortUrlAndReturnCreatedShortUrlDto_Returns_201() throws Exception {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .url("example.com")
                .alias("ex")
                .build();
        final String redirectingUrl = String.format("%s/%s", testClientUrl, shortUrlDto.getAlias());
        final HttpEntity<ShortUrlDto> request = createRequest(shortUrlDto);

        final ResponseEntity<ShortUrlDto> response = restTemplate.postForEntity(
                testServerUrl + "/api/short_urls",
                request,
                ShortUrlDto.class
        );
        final ShortUrlDto shortUrlDtoInResponse = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(shortUrlDtoInResponse.getUrl(), shortUrlDto.getUrl()),
                () -> assertEquals(shortUrlDtoInResponse.getAlias(), shortUrlDto.getAlias()),
                () -> assertEquals(redirectingUrl, shortUrlDtoInResponse.getRedirectingUrl()),
                () -> assertNotEquals(0, shortUrlDtoInResponse.getId()),
                () -> assertNotNull(shortUrlDtoInResponse.getCreatedAt())
        );
    }

    /**
     * Should be run after shouldCreateNewShortUrlAndReturnCreatedShortUrlDto_Returns_201
     */
    @Test
    @Order(2)
    void shouldFailOnCreatingShortUrlWhenTakenAlias_Returns_400() throws Exception {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .url("example.com")
                .alias("ex")
                .build();
        final HttpEntity<ShortUrlDto> request = createRequest(shortUrlDto);

        final ResponseEntity<String> response = restTemplate.postForEntity(
                testServerUrl + "/api/short_urls",
                request,
                String.class
        );
        final Error error = convertToPojo(response.getBody(), Error.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(1, error.getErrors().size()),
                () -> assertEquals("alias", error.getErrors().get(0).getField())
        );
    }

    /**
     * Should be run after shouldCreateNewShortUrlAndReturnCreatedShortUrlDto_Returns_201
     */
    @Test
    @Order(3)
    void shouldGetAllShortUrls_Returns_200() throws JsonProcessingException {
        final ResponseEntity<String> response = restTemplate.getForEntity(
                testServerUrl + "/api/short_urls",
                String.class
        );
        final ShortUrlDto[] shortUrlDtos = convertToPojo(response.getBody(), ShortUrlDto[].class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(1, shortUrlDtos.length)
        );
    }

    @Test
    void shouldFailOnCreatingShortUrlWhenNoRequiredUrlField_Returns_400() throws Exception {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .alias("ex3")
                .build();
        final HttpEntity<ShortUrlDto> request = createRequest(shortUrlDto);

        final ResponseEntity<String> response = restTemplate.postForEntity(
                testServerUrl + "/api/short_urls",
                request,
                String.class
        );
        final Error error = convertToPojo(response.getBody(), Error.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(1, error.getErrors().size()),
                () -> assertEquals("url", error.getErrors().get(0).getField())
        );
    }

    @Test
    void shouldCreateRandomAliasWhenNoAliasPresent_Returns_201() {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .url("example.com")
                .build();
        final HttpEntity<ShortUrlDto> request = createRequest(shortUrlDto);

        final ResponseEntity<ShortUrlDto> response = restTemplate.postForEntity(
                testServerUrl + "/api/short_urls",
                request,
                ShortUrlDto.class
        );
        final ShortUrlDto shortUrlDtoInResponse = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(shortUrlDtoInResponse.getUrl(), shortUrlDto.getUrl()),
                () -> assertNotNull(shortUrlDtoInResponse.getAlias()),
                () -> assertNotNull(shortUrlDtoInResponse.getRedirectingUrl()),
                () -> assertNotEquals(0, shortUrlDtoInResponse.getId()),
                () -> assertNotNull(shortUrlDtoInResponse.getCreatedAt())
        );
    }

    private <T> HttpEntity<T> createRequest(final T requestedBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestedBody, headers);
    }

    private <T> T convertToPojo(final String jsonString, Class clazz) throws JsonProcessingException {
        return (T) objectMapper.readValue(jsonString, clazz);
    }

    public <T> List<T> getApi(final String path, final HttpMethod method) {
        final ResponseEntity<List<T>> response = restTemplate.exchange(
                path,
                method,
                null,
                new ParameterizedTypeReference<List<T>>(){});
        List<T> list = response.getBody();
        return list;
    }
}
