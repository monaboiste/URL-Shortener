package com.github.monaboiste.urlshortener.persistence.repository;

import com.github.monaboiste.urlshortener.persistence.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    boolean existsByAlias(final String alias);
}
