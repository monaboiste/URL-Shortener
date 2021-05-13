package com.github.monaboiste.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AliasGeneratorService {

    // With 6 characters we have about 28^6 (approx. 480 mln) possible ids:
    // 26 alphabet letters + '-' and '_' characters.
    private static final int CHARACTERS = 6;
    private final SecureRandom random;

    public String generateRandomAlias() {
        final byte[] bytes = new byte[CHARACTERS];
        random.nextBytes(bytes);
        final Base64.Encoder encoder = Base64.getUrlEncoder()
                .withoutPadding();
        return encoder.encodeToString(bytes);
    }
}
