package com.github.monaboiste.urlshortener.service;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import com.github.monaboiste.urlshortener.persistence.repository.ShortUrlRepository;
import com.github.monaboiste.urlshortener.utils.ShortUrlConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlDto createShortUrl(final ShortUrlDto shortUrlDto) {
        ShortUrl shortUrl = ShortUrlConverter.convertToEntity(shortUrlDto);
        shortUrl.setCreatedAt(OffsetDateTime.now());

        final ShortUrl persistedShortUrl = shortUrlRepository.save(shortUrl);
        return ShortUrlConverter.convertToDto(persistedShortUrl);
    }
}
