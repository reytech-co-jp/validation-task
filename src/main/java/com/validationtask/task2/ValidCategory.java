package com.validationtask.task2;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@ReportAsSingleViolation
public @interface ValidCategory {
    public String message() default "{ValidCategory}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
