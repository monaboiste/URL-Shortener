package com.github.monaboiste.urlshortener.utils;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShortUrlConverter {

    public static ShortUrl convertToEntity(final ShortUrlDto shortUrlDto) {
        final ShortUrl shortUrl = ShortUrl.builder()
                .id(shortUrlDto.getId())
                .url(shortUrlDto.getUrl())
                .alias(shortUrlDto.getAlias())
                .build();
        return shortUrl;
    }

    public static ShortUrlDto convertToDto(final ShortUrl shortUrl) {
        final LocalDateTime localDateTime = DateTimeZoneConverter
                .convertUtcToLocalDateTime(shortUrl.getCreatedAt());
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(shortUrl.getId())
                .url(shortUrl.getUrl())
                .alias(shortUrl.getAlias())
                .createdAt(localDateTime)
                .build();
        return shortUrlDto;
    }
}
