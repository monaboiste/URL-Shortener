package com.github.monaboiste.urlshortener.controller;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.service.ShortUrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/short_urls")
@AllArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @PostMapping
    public ResponseEntity<ShortUrlDto> createShortUrl(@RequestBody final ShortUrlDto shortUrlDto) {
        final ShortUrlDto shortUrl = shortUrlService.createShortUrl(shortUrlDto);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(shortUrl)
                .toUri();

        final ResponseEntity<ShortUrlDto> response = ResponseEntity.created(location)
                .body(shortUrl);

        return response;
    }
}
