package com.github.monaboiste.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShortUrlDto {

    private long id;
    private String url;
    private String alias;
    private String redirectingUrl;
    private LocalDateTime createdAt;
}
