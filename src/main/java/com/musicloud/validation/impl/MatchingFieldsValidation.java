package com.musicloud.validation.impl;

import com.musicloud.validation.MatchingFields;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchingFieldsValidation implements ConstraintValidator<MatchingFields, Object> {
    private String first;
    private String second;
    private MatchingFields constraintAnnotation;

    @Override
    public void initialize(MatchingFields constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();

        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
        Object x = beanWrapper.getPropertyValue(first);
        Object y = beanWrapper.getPropertyValue(second);

        boolean valid;
        if (x == null) valid = y == null;
        else valid = x.equals(y);

        if (!valid) {
            context.buildConstraintViolationWithTemplate(constraintAnnotation.message())
                    .addPropertyNode(second)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;

    }
}
