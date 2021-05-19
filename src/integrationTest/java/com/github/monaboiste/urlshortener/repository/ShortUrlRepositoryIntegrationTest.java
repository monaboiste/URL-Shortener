package com.github.monaboiste.urlshortener.repository;

import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import com.github.monaboiste.urlshortener.persistence.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("integration")
@DataJpaTest
public class ShortUrlRepositoryIntegrationTest {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Test
    void shouldCheck_whenExistsByAlias() {
        final String validAlias = "sample-1";
        final ShortUrl shortUrlWithValidAlias = ShortUrl.builder()
                .url("example.com")
                .alias(validAlias)
                .createdAt(OffsetDateTime.of(2021, 5, 1, 10, 0 ,0 ,0 , ZoneOffset.UTC))
                .build();
        shortUrlRepository.save(shortUrlWithValidAlias);

        boolean existsByAlias = shortUrlRepository.existsByAlias(validAlias);

        assertThat(existsByAlias).isTrue();
    }

    @Test
    void shouldCheck_whenNotExistsByAlias() {
        final String nonExistingAlias = "exemplary-alias";

        boolean existsByAlias = shortUrlRepository.existsByAlias(nonExistingAlias);

        assertThat(existsByAlias).isFalse();
    }
}
