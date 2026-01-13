package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PolishPlateValidator implements ConstraintValidator<ValidPolishPlate, String> {

    private static final Pattern STANDARD_PLATE =
            Pattern.compile("^[A-Z]{2,3}[A-Z0-9]{1,5}$");

    private static final Pattern INDIVIDUAL_PLATE =
            Pattern.compile("^[A-Z][0-9][A-Z0-9]{3,5}$");

    @Override
    public boolean isValid(String plate, ConstraintValidatorContext context) {
        if (plate == null || plate.isBlank()) return true;

        plate = plate.toUpperCase();

        if (plate.length() < 5 || plate.length() > 8) {
            setMessage(context, "{car.registrationNumber.length}");
            return false;
        }

        if (STANDARD_PLATE.matcher(plate).matches()) {
            return true;
        }

        if (INDIVIDUAL_PLATE.matcher(plate).matches()) {
            return true;
        }

        setMessage(context, "{car.registrationNumber.format}");
        return false;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
