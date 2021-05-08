package com.github.monaboiste.urlshortener.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeZoneConverter {

    public static OffsetDateTime convertLocalDateTimeToUtc(final LocalDateTime localDateTime) {
        return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
    }

    public static LocalDateTime convertUtcToLocalDateTime(final OffsetDateTime offsetDateTime) {
        return offsetDateTime.toLocalDateTime();
    }
}
