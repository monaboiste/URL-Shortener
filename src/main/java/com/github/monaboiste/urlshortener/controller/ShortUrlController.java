package com.github.monaboiste.urlshortener.controller;

import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/short_urls")
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @PostMapping
    public ResponseEntity<ShortUrlDto> createShortUrl(
            @Valid @RequestBody final ShortUrlDto shortUrlDto) {
        final ShortUrlDto shortUrl = shortUrlService.createShortUrl(shortUrlDto);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(shortUrl)
                .toUri();

        final ResponseEntity<ShortUrlDto> response = ResponseEntity.created(location)
                .body(shortUrl);

        return response;
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShortUrlDto> getAllShortUrls() {
        return shortUrlService.getAllShortUrls();
    }
}
