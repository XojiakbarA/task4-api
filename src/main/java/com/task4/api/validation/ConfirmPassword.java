package com.task4.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Documented
public @interface ConfirmPassword {

    String message() default "The password confirmation does not match.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
