package com.github.monaboiste.urlshortener.validation;

import com.github.monaboiste.urlshortener.persistence.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueAliasValidator implements ConstraintValidator<UniqueAlias, String> {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Override
    public boolean isValid(String shortUrlAlias, ConstraintValidatorContext context) {
        return !shortUrlRepository.existsByAlias(shortUrlAlias);
    }

}
