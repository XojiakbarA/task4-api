package com.task4.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, Object> {
    @Override
    public void initialize(ConfirmPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        final RegisterRequest request = (RegisterRequest) o;
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
