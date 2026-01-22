package pl.kurs.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PolishPlateValidatorTest {
    private final PolishPlateValidator validator = new PolishPlateValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @Test
    void shouldReturnTrueWhenPlateNumberIsNull() {
        //when
        boolean result = validator.isValid(null, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenPlateNumberIsBlank() {
        //when
        boolean result = validator.isValid("    ", context);

        //then
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"WA1", "WA1234567890"})
    void shouldReturnFalseWhenPlateNumberLengthIsInvalid(String invalidVin) {
        //given
        stubConstraintViolation();

        //when
        boolean result = validator.isValid(invalidVin, context);

        //then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("{car.registrationNumber.length}");
    }

    @ParameterizedTest
    @ValueSource(strings = {"W1ABC", "WA12345"})
    void shouldReturnTrueWhenPlateNumberIsValid(String validVin) {
        //when
        boolean result = validator.isValid(validVin, context);

        //then
        assertTrue(result);
     }

    @Test
    void shouldReturnFalseWhenPlateNumberFormatIsInvalid() {
        //given
        String invalidFormat = "12345";
        stubConstraintViolation();

        //when
        boolean result = validator.isValid(invalidFormat, context);

        //then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("{car.registrationNumber.format}");
    }

    private void stubConstraintViolation() {
        when(context.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(builder);
        when(builder.addConstraintViolation())
                .thenReturn(context);
    }
}