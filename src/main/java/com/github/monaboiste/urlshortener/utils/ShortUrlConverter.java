package com.github.monaboiste.urlshortener.utils;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static ShortUrlDto convertToDto(final ShortUrl shortUrl, final String domainUrl) {
        final LocalDateTime localDateTime = DateTimeZoneConverter
                .convertUtcToLocalDateTime(shortUrl.getCreatedAt());
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(shortUrl.getId())
                .url(shortUrl.getUrl())
                .alias(shortUrl.getAlias())
                .redirectingUrl(String.format("%s/%s", domainUrl, shortUrl.getAlias()))
                .createdAt(localDateTime)
                .build();
        return shortUrlDto;
    }

    public static List<ShortUrlDto> convertToDtoList(final List<ShortUrl> shortUrls,
                                                     final String domainUrl) {
        List<ShortUrlDto> dtos = shortUrls.stream()
                .map(url -> convertToDto(url, domainUrl))
                .collect(Collectors.toList());
        return dtos;
    }
}
