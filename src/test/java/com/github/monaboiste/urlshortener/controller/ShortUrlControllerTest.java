package com.github.monaboiste.urlshortener.controller;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.service.ShortUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    @Autowired
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

        mockMvc.perform(post("/short_urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(shortUrlDtoResponse.getId()))
                .andExpect(jsonPath("$.url").value(shortUrlDtoResponse.getUrl()))
                .andExpect(jsonPath("$.alias").value(shortUrlDtoResponse.getAlias()))
                .andExpect(jsonPath("$.redirectingUrl").value(shortUrlDtoResponse.getRedirectingUrl()))
                .andExpect(jsonPath("$.createdAt").value(serializeLocalDateTime(shortUrlDtoResponse.getCreatedAt())));

        verify(shortUrlService, times(1)).createShortUrl(any(ShortUrlDto.class));
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
