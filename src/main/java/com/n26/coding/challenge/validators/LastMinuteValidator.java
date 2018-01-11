package com.n26.coding.challenge.validators;

import java.time.Duration;
import java.util.function.Supplier;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.n26.coding.challenge.annotations.LastMinute;

public class LastMinuteValidator implements ConstraintValidator<LastMinute, Long> {

    public static Supplier<Long> NOW = System::currentTimeMillis;

    private LastMinute annotation;

    @Override
    public void initialize(LastMinute annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Duration age = Duration.of(annotation.duration(), annotation.unit());
        return value == null || NOW.get() - value <= age.toMillis();
    }
}
