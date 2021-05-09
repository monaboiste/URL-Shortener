package com.github.monaboiste.urlshortener.repository;

import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import com.github.monaboiste.urlshortener.persistence.repository.ShortUrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class ShortUrlRepositoryTest {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    private ShortUrl shortUrl;

    @BeforeEach
    void setUp() {
        shortUrl = ShortUrl.builder()
                .id(1)
                .url("example.com")
                .alias("ex")
                .createdAt(OffsetDateTime.now())
                .build();
    }

    @Test
    void shouldCheckIfExistsByAlias() {
        shortUrlRepository.save(shortUrl);

        boolean existsByAlias = shortUrlRepository.existsByAlias(shortUrl.getAlias());

        assertTrue(existsByAlias);
    }

    @Test
    void shouldCheckIfNotExistsByAlias() {
        boolean existsByAlias = shortUrlRepository.existsByAlias(shortUrl.getAlias());

        assertFalse(existsByAlias);
    }
}
