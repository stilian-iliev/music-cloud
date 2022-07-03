package com.musicloud.validation;

import com.musicloud.validation.impl.MatchingFieldsValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = MatchingFieldsValidation.class)
public @interface MatchingFields {
    String first();
    String second();
    String message() default "Fields not matching";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
