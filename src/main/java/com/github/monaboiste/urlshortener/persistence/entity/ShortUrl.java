package com.github.monaboiste.urlshortener.persistence.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "short_urls")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "alias", nullable = false, unique = true)
    private String alias;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
