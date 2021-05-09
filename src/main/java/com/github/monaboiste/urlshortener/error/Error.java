package com.github.monaboiste.urlshortener.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Error {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class FieldError {

        private String field;
        private String message;
    }

    private final List<FieldError> errors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        errors.add(new FieldError(field, message));
    }
}
