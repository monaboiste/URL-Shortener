package com.github.monaboiste.urlshortener.dto;

import com.github.monaboiste.urlshortener.validation.UniqueAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShortUrlDto {

    private long id;

    @NotBlank
    private String url;

    @NotBlank
    @UniqueAlias
    private String alias;

    private String redirectingUrl;

    private LocalDateTime createdAt;
}
