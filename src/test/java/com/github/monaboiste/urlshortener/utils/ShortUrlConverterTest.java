package com.github.monaboiste.urlshortener.utils;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class ShortUrlConverterTest {

    private static final String DOMAIN_URL = "http://localhost";

    @Test
    void shouldConvertShortUrlDtoToEntity() {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .build();

        final ShortUrl shortUrl = ShortUrlConverter.convertToEntity(shortUrlDto);

        assertAll(
                () -> assertEquals(shortUrlDto.getId(), shortUrl.getId()),
                () -> assertEquals(shortUrlDto.getUrl(), shortUrl.getUrl()),
                () -> assertEquals(shortUrlDto.getAlias(), shortUrl.getAlias())
        );
    }

    @Test
    void shouldConvertShortUrlEntityToDto() {
        final OffsetDateTime utcDateTime = OffsetDateTime.now();
        final LocalDateTime localDateTime = DateTimeZoneConverter.convertUtcToLocalDateTime(utcDateTime);
        final ShortUrl shortUrl = ShortUrl.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .createdAt(utcDateTime)
                .build();

        final ShortUrlDto shortUrlDto = ShortUrlConverter.convertToDto(shortUrl, DOMAIN_URL);

        assertAll(
                () -> assertEquals(shortUrl.getId(), shortUrlDto.getId()),
                () -> assertEquals(shortUrl.getUrl(), shortUrlDto.getUrl()),
                () -> assertEquals(shortUrl.getAlias(), shortUrlDto.getAlias()),
                () -> assertEquals(localDateTime, shortUrlDto.getCreatedAt())
        );
    }

    @Test
    void shouldConvertShortUrlEntityListToDtoList() {
        final OffsetDateTime utcDateTime = OffsetDateTime.now();
        final LocalDateTime localDateTime = DateTimeZoneConverter.convertUtcToLocalDateTime(utcDateTime);
        final List<ShortUrl> shortUrls = Arrays.asList(ShortUrl.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .createdAt(utcDateTime)
                .build()
        );

        final List<ShortUrlDto> shortUrlDtoList = ShortUrlConverter.convertToDtoList(shortUrls, DOMAIN_URL);

        assertAll(
                () -> assertEquals(shortUrls.get(0).getId(), shortUrlDtoList.get(0).getId()),
                () -> assertEquals(shortUrls.get(0).getUrl(), shortUrlDtoList.get(0).getUrl()),
                () -> assertEquals(shortUrls.get(0).getAlias(), shortUrlDtoList.get(0).getAlias()),
                () -> assertEquals(localDateTime, shortUrlDtoList.get(0).getCreatedAt())
        );
    }
}