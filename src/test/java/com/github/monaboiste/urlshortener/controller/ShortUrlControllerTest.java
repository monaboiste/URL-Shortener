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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void shouldCreateNewShortUrlAndReturnCreatedShortUrlDto() throws Exception{
        final String payload = "{\n" +
                "   \"url\": \"example.com\"," +
                "   \"alias\": \"ex\"" +
                "}";

        final ShortUrlDto shortUrlDtoResponse = ShortUrlDto.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .redirectingUrl("http://localhost/ex")
                .createdAt(LocalDateTime.now())
                .build();

        when(shortUrlService.createShortUrl(any(ShortUrlDto.class)))
                .thenReturn(shortUrlDtoResponse);
        ResultActions resultActions = mockMvc.perform(post("/short_urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andDo(print());

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(shortUrlDtoResponse.getId()))
                .andExpect(jsonPath("$.url", is(shortUrlDtoResponse.getUrl())))
                .andExpect(jsonPath("$.alias", is(shortUrlDtoResponse.getAlias())))
                .andExpect(jsonPath("$.redirectingUrl", is(shortUrlDtoResponse.getRedirectingUrl())))
                .andExpect(jsonPath("$.createdAt", is(
                        serializeLocalDateTime(shortUrlDtoResponse.getCreatedAt()))));

        verify(shortUrlService, times(1)).createShortUrl(any(ShortUrlDto.class));
    }

    @Test
    public void shouldFailWhenNoRequiredFieldsPresent() throws Exception{
        final String payload = "{\n" +
                "\"alias\": \"ex\"," +
                "}";

        ResultActions resultActions = mockMvc.perform(post("/short_urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andDo(print());

        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetShortUrlList() throws Exception{
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .redirectingUrl("http://localhost/ex")
                .createdAt(LocalDateTime.now())
                .build();

        when(shortUrlService.getAllShortUrls()).thenReturn(Arrays.asList(shortUrlDto));
        ResultActions resultActions = mockMvc.perform(get("/short_urls")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(shortUrlDto.getId()))
                .andExpect(jsonPath("$[0].url", is(shortUrlDto.getUrl())))
                .andExpect(jsonPath("$[0].alias", is(shortUrlDto.getAlias())))
                .andExpect(jsonPath("$[0].redirectingUrl", is(shortUrlDto.getRedirectingUrl())))
                .andExpect(jsonPath("$[0].createdAt", is(
                        serializeLocalDateTime(shortUrlDto.getCreatedAt()))));

        verify(shortUrlService, times(1)).getAllShortUrls();
    }


    /**
     * Method converts LocalDateTime to List<Integer>.as
     * {@link org.springframework.test.web.servlet.result.MockMvcResultMatchers#jsonPath}
     * serializes Json String Date to [yyyy,M,dd,H,m,ss,n]
     * example:
     * 2021-05-10T23:42:33.796997500 -> [2021,5,10,23,42,33,796997500]
     *
     * @param localDateTime
     * @return List<Integer> [yyyy,M,dd,H,m,ss,n] pattern
     */
    private static List<Integer> serializeLocalDateTime(final LocalDateTime localDateTime) {
        return Arrays.stream(localDateTime.toString()
                .split("[-:.T]"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }


}
