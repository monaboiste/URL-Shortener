package com.github.monaboiste.urlshortener.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(final JsonParser parser,
                                      final DeserializationContext context)
                                      throws IOException, JsonProcessingException {
        return OffsetDateTime.ofInstant(
                Instant.ofEpochMilli(parser.getLongValue()), ZoneOffset.UTC);
    }
}
