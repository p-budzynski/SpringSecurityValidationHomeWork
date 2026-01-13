package pl.kurs.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearInRangeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface YearInRange {

    String message() default "{car.yearOfProduction.yearInRange}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
