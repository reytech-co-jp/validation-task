package com.validationtask.task2;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@NotBlank
@Size(min = 2, max = 20)
public @interface ValidSeller {
    public String message() default "{ValidSeller}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
