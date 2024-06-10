package com.ntt.transaction.util;


import java.math.BigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NonZeroValidator implements ConstraintValidator<NonZero, BigDecimal> {

    @Override
    public void initialize(NonZero constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull se encargará de esto
        }
        return value.compareTo(BigDecimal.ZERO) != 0;
    }
}
