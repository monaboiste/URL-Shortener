package com.github.monaboiste.urlshortener.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeSerializer extends JsonSerializer<OffsetDateTime> {

    @Override
    public void serialize(final OffsetDateTime dateTime,
                          final JsonGenerator jsonGenerator,
                          final SerializerProvider provider) throws IOException {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ssO")
                .withZone(ZoneOffset.UTC);
        jsonGenerator.writeString(dateTimeFormatter.format(dateTime));
    }
}
