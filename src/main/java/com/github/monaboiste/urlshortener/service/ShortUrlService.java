package com.github.monaboiste.urlshortener.service;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import com.github.monaboiste.urlshortener.persistence.repository.ShortUrlRepository;
import com.github.monaboiste.urlshortener.utils.ShortUrlConverter;
import com.github.monaboiste.urlshortener.validation.UniqueAliasValidator;
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
    private final AliasGeneratorService aliasGeneratorService;
    private final UniqueAliasValidator uniqueAliasValidator;

    public ShortUrlDto createShortUrl(final ShortUrlDto shortUrlDto) {
        ShortUrl shortUrl = ShortUrlConverter.convertToEntity(shortUrlDto);
        shortUrl.setCreatedAt(OffsetDateTime.now());

        if (shortUrl.getAlias() == null) {
            setRandomGeneratedAlias(shortUrl);
        }

        final ShortUrl persistedShortUrl = shortUrlRepository.save(shortUrl);

        return ShortUrlConverter.convertToDto(persistedShortUrl, domainUrl);
    }

    private void setRandomGeneratedAlias(final ShortUrl shortUrl) {
        String alias = aliasGeneratorService.generateRandomAlias();
        while (!uniqueAliasValidator.isValid(alias, null)) {
            alias = aliasGeneratorService.generateRandomAlias();
        }
        shortUrl.setAlias(alias);
    }
}
