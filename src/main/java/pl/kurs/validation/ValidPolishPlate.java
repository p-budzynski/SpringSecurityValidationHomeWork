package pl.kurs.validation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PolishPlateValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPolishPlate {

    String message() default "Invalid Polish registration plate number";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
