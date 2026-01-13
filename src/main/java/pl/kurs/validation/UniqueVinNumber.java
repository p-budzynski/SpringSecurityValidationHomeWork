package pl.kurs.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueVinNumberValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueVinNumber {

    String message() default "{car.vinNumber.uniqueVinNumber}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
