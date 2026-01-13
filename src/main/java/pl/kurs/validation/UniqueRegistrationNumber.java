package pl.kurs.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueRegistrationNumberValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueRegistrationNumber {

    String message() default "{car.registrationNumber.uniqueRegistrationNumber}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
