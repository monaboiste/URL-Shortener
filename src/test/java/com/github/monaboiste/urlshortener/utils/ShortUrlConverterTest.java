package com.github.monaboiste.urlshortener.utils;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
class ShortUrlConverterTest {

    @Test
    void shouldConvertShortUrlDtoToEntity() {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(1L)
                .url("example.com")
                .alias("ex")
                .redirectingUrl("http://localhost:8080/ex")
                .createdAt(OffsetDateTime.of(2021, 5, 1, 10, 0, 0, 0, ZoneOffset.UTC))
                .build();

        final ShortUrl shortUrl = ShortUrlConverter.convertToEntity(shortUrlDto);

        assertAll(
                () -> assertThat(shortUrlDto.getId()).isEqualTo(shortUrl.getId()),
                () -> assertThat(shortUrlDto.getUrl()).isEqualTo(shortUrl.getUrl()),
                () -> assertThat(shortUrlDto.getAlias()).isEqualTo(shortUrl.getAlias()),
                () -> assertThat(shortUrlDto.getRedirectingUrl()).isEqualTo(shortUrl.getRedirectingUrl()),
                () -> assertThat(shortUrlDto.getCreatedAt()).isEqualTo(shortUrl.getCreatedAt())
        );
    }

    @Test
    void shouldConvertShortUrlEntityToDto() {
        final ShortUrl shortUrl = ShortUrl.builder()
                .id(1L)
                .url("example.com")
                .alias("ex")
                .redirectingUrl("http://localhost:8080/ex")
                .createdAt(OffsetDateTime.of(2021, 5, 1, 10, 0, 0, 0, ZoneOffset.UTC))
                .build();

        final ShortUrlDto shortUrlDto = ShortUrlConverter.convertToDto(shortUrl);

        assertAll(
                () -> assertThat(shortUrlDto.getId()).isEqualTo(shortUrl.getId()),
                () -> assertThat(shortUrlDto.getUrl()).isEqualTo(shortUrl.getUrl()),
                () -> assertThat(shortUrlDto.getAlias()).isEqualTo(shortUrl.getAlias()),
                () -> assertThat(shortUrlDto.getRedirectingUrl()).isEqualTo(shortUrl.getRedirectingUrl()),
                () -> assertThat(shortUrlDto.getCreatedAt()).isEqualTo(shortUrl.getCreatedAt())
        );
    }

    @Test
    void shouldConvertShortUrlEntityListToDtoList() {
        final List<ShortUrl> shortUrls = Arrays.asList(ShortUrl.builder()
                .id(1L)
                .url("example.com")
                .alias("ex")
                .redirectingUrl("http://localhost:8080/ex")
                .createdAt(OffsetDateTime.of(2021, 5, 1, 10, 0, 0, 0, ZoneOffset.UTC))
                .build()
        );

        final List<ShortUrlDto> shortUrlDtos = ShortUrlConverter.convertToDtoList(shortUrls);

        assertAll(
                () -> assertThat(shortUrls.get(0).getId()).isEqualTo(shortUrlDtos.get(0).getId()),
                () -> assertThat(shortUrls.get(0).getUrl()).isEqualTo(shortUrlDtos.get(0).getUrl()),
                () -> assertThat(shortUrls.get(0).getAlias()).isEqualTo(shortUrlDtos.get(0).getAlias()),
                () -> assertThat(shortUrls.get(0).getCreatedAt()).isEqualTo(shortUrlDtos.get(0).getCreatedAt()),
                () -> assertThat(shortUrls.get(0).getCreatedAt()).isEqualTo(shortUrlDtos.get(0).getCreatedAt())
        );
    }
}