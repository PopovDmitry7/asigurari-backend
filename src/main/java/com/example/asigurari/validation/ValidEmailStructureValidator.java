package com.example.asigurari.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEmailStructureValidator implements ConstraintValidator<ValidEmailStructure, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !value.contains("@") || !value.contains(".")) {
            return false;
        }

        int atIndex = value.indexOf("@");
        int dotIndex = value.indexOf(".", atIndex);

        // Must have at least 3 characters between @ and .
        return dotIndex - atIndex > 3;
    }
}