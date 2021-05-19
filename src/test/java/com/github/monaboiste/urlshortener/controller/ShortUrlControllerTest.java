package com.github.monaboiste.urlshortener.controller;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.service.ShortUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ShortUrlControllerTest {

    @Mock
    private ShortUrlService shortUrlService;

    @Mock
    private LocalValidatorFactoryBean validator;

    @InjectMocks
    private ShortUrlController shortUrlController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shortUrlController)
                .setValidator(validator)
                .build();
    }

    /**
     * It should fail if (eventually) ShortUrlDto class is extended with new required fields.
     */
    @Test
    public void shouldCreateShortUrlDto_whenValidShortUrl_then201_returnCreatedShortUrlDto() throws Exception{
        final String payload = "{\n" +
                "   \"url\": \"example.com\"," +
                "   \"alias\": \"ex\"" +
                "}";

        final ShortUrlDto expected = ShortUrlDto.builder()
                .id(1L)
                .url("example.com")
                .alias("ex")
                .redirectingUrl("http://localhost/ex")
                .createdAt(OffsetDateTime.of(2021, 5, 1, 10, 0, 0, 0, ZoneOffset.UTC))
                .build();

        given(shortUrlService.createShortUrl(any(ShortUrlDto.class))).willReturn(expected);

        final ResultActions resultActions = mockMvc.perform(
                post("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andDo(print());

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.url", is(expected.getUrl())))
                .andExpect(jsonPath("$.alias", is(expected.getAlias())))
                .andExpect(jsonPath("$.redirectingUrl", is(expected.getRedirectingUrl())))
                .andExpect(jsonPath("$.createdAt", is(formatDateTime(expected.getCreatedAt()))));

        then(shortUrlService).should(times(1)).createShortUrl(any(ShortUrlDto.class));
    }

    @Test
    public void shouldFail_whenNoRequiredUrlFieldPresent_then400() throws Exception{
        final String payload = "{\n" +
                "\"alias\": \"ex\"," +
                "}";

        final ResultActions resultActions = mockMvc.perform(
                post("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andDo(print());

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetAllShortUrls_then200() throws Exception{
        final List<ShortUrlDto> expected = Arrays.asList(
                ShortUrlDto.builder()
                    .id(1L)
                    .url("example.com")
                    .alias("ex")
                    .redirectingUrl("http://localhost/ex")
                    .createdAt(OffsetDateTime.of(2021, 5, 1, 10, 0, 0, 0, ZoneOffset.UTC))
                    .build()
        );
        given(shortUrlService.getAllShortUrls()).willReturn(expected);

        final ResultActions resultActions = mockMvc.perform(
                get("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(expected.get(0).getId()))
                .andExpect(jsonPath("$[0].url", is(expected.get(0).getUrl())))
                .andExpect(jsonPath("$[0].alias", is(expected.get(0).getAlias())))
                .andExpect(jsonPath("$[0].redirectingUrl", is(expected.get(0).getRedirectingUrl())))
                .andExpect(jsonPath("$[0].createdAt", is(
                        formatDateTime(expected.get(0).getCreatedAt()))));

        then(shortUrlService).should(times(1)).getAllShortUrls();
    }

    /**
     * Method formats date to "yyy-MM-dd'T'HH:mm:ssXXX" pattern
     * as {@link ShortUrlDto} has set @JsonFormat on createdAt field
     *
     * @param dateTime
     * @return String formatted as "yyy-MM-dd'T'HH:mm:ssXXX"
     */
    private static String formatDateTime(final OffsetDateTime dateTime) {
        final String pattern = "yyy-MM-dd'T'HH:mm:ssXXX";
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
