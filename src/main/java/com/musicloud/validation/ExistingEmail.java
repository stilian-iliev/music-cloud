package com.musicloud.validation;

import com.musicloud.validation.impl.ExistingEmailValidation;
import com.musicloud.validation.impl.UniqueEmailValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ExistingEmailValidation.class)
public @interface ExistingEmail {

    String message() default "Invalid User";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
