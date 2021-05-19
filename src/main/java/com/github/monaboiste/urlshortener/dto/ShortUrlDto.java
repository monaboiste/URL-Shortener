package com.github.monaboiste.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.monaboiste.urlshortener.utils.DateTimeDeserializer;
import com.github.monaboiste.urlshortener.utils.DateTimeSerializer;
import com.github.monaboiste.urlshortener.validation.UniqueAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShortUrlDto {

    @JsonProperty
    private long id;

    @NotBlank
    @JsonProperty(required = true)
    private String url;

    @Nullable
    @UniqueAlias
    @JsonProperty
    private String alias;

    @JsonProperty
    private String redirectingUrl;

    @JsonProperty
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private OffsetDateTime createdAt;
}
