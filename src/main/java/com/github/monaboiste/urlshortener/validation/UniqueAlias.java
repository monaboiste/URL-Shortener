package com.github.monaboiste.urlshortener.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueAliasValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueAlias {
    String message() default "{com.github.monaboiste.urlshortener.validation.UniqueAlias.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
