package com.github.monaboiste.urlshortener.utils;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShortUrlConverter {

    public static ShortUrl convertToEntity(final ShortUrlDto shortUrlDto) {
        final ShortUrl shortUrl = ShortUrl.builder()
                .id(shortUrlDto.getId())
                .url(shortUrlDto.getUrl())
                .alias(shortUrlDto.getAlias())
                .redirectingUrl(shortUrlDto.getRedirectingUrl())
                .createdAt(shortUrlDto.getCreatedAt())
                .build();
        return shortUrl;
    }

    public static ShortUrlDto convertToDto(final ShortUrl shortUrl) {
        final ShortUrlDto shortUrlDto = ShortUrlDto.builder()
                .id(shortUrl.getId())
                .url(shortUrl.getUrl())
                .alias(shortUrl.getAlias())
                .redirectingUrl(shortUrl.getRedirectingUrl())
                .createdAt(shortUrl.getCreatedAt())
                .build();
        return shortUrlDto;
    }

    public static List<ShortUrlDto> convertToDtoList(final List<ShortUrl> shortUrls) {
        List<ShortUrlDto> dtos = shortUrls.stream()
                .map(ShortUrlConverter::convertToDto)
                .collect(Collectors.toList());
        return dtos;
    }
}
