package com.musicloud.validation;

import com.musicloud.validation.impl.MatchingFieldsValidation;
import com.musicloud.validation.impl.PasswordValidation;
import com.musicloud.validation.impl.UniqueEmailValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordValidation.class)
public @interface CorrectPassword {
    String message() default "Wrong password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
