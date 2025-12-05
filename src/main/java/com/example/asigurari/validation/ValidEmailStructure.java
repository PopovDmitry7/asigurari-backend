package com.example.asigurari.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidEmailStructureValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailStructure {
    String message() default "Emailul trebuie să aibă cel puțin 3 caractere între '@' și '.'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}