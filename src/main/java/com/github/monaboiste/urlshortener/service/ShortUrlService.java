package com.github.monaboiste.urlshortener.service;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import com.github.monaboiste.urlshortener.persistence.repository.ShortUrlRepository;
import com.github.monaboiste.urlshortener.utils.ShortUrlConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    @Value("${client.domain}:${client.port}")
    private String domainUrl;

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlDto createShortUrl(final ShortUrlDto shortUrlDto) {
        ShortUrl shortUrl = ShortUrlConverter.convertToEntity(shortUrlDto);
        shortUrl.setCreatedAt(OffsetDateTime.now());

        final ShortUrl persistedShortUrl = shortUrlRepository.save(shortUrl);

        ShortUrlDto shortUrlDtoResponse = ShortUrlConverter.convertToDto(persistedShortUrl);
        shortUrlDtoResponse.setRedirectingUrl(
                String.format("%s/%s", domainUrl, shortUrl.getAlias())
        );
        return shortUrlDtoResponse;
    }
}
