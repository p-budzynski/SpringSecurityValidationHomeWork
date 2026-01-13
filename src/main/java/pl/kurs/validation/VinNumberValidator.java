package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class VinNumberValidator implements ConstraintValidator<VinNumber, String> {

    private static final Map<Character, Integer> TRANSLITERATION = Map.ofEntries(
            Map.entry('0', 0), Map.entry('1', 1), Map.entry('2', 2),
            Map.entry('3', 3), Map.entry('4', 4), Map.entry('5', 5),
            Map.entry('6', 6), Map.entry('7', 7), Map.entry('8', 8),
            Map.entry('9', 9),

            Map.entry('A', 1), Map.entry('B', 2), Map.entry('C', 3),
            Map.entry('D', 4), Map.entry('E', 5), Map.entry('F', 6),
            Map.entry('G', 7), Map.entry('H', 8),
            Map.entry('J', 1), Map.entry('K', 2), Map.entry('L', 3),
            Map.entry('M', 4), Map.entry('N', 5),
            Map.entry('P', 7), Map.entry('R', 9),
            Map.entry('S', 2), Map.entry('T', 3), Map.entry('U', 4),
            Map.entry('V', 5), Map.entry('W', 6), Map.entry('X', 7),
            Map.entry('Y', 8), Map.entry('Z', 9)
    );

    private static final int[] WEIGHTS = {
            8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2
    };

    @Override
    public boolean isValid(String vin, ConstraintValidatorContext context) {
        if (vin == null || vin.isBlank()) return true;

        vin = vin.toUpperCase();

        if (vin.length() != 17) {
            setMessage(context, "{car.vinNumber.length}");
            return false;
        }

        int sum = 0;

        for (int i = 0; i < vin.length(); i++) {
            char c = vin.charAt(i);

            if (!TRANSLITERATION.containsKey(c)) {
                setMessage(context, "{car.vinNumber.contains}");
                return false;
            }

            sum += TRANSLITERATION.get(c) * WEIGHTS[i];
        }

        int remainder = sum % 11;
        char expectedCheckDigit = (remainder == 10) ? 'X' : (char) ('0' + remainder);

        if (vin.charAt(8) != expectedCheckDigit) {
            setMessage(context, "{car.vinNumber.check}");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}
