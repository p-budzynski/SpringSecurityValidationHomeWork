package pl.kurs.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VinNumberValidatorTest {
    private final VinNumberValidator validator = new VinNumberValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @ParameterizedTest
    @ValueSource(strings = {"1M8GDM9AXKP042788", "5GZCZ43D13S812715"})
    void shouldReturnTrueWhenVinNumberIsValid(String correctVin) {
        //when
        boolean result = validator.isValid(correctVin, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenVinNumberIsNull() {
        //when
        boolean result = validator.isValid(null, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenVinNumberIsBlank() {
        //when
        boolean result = validator.isValid("    ", context);

        //then
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"SHORT1234", "LONG12345678901234567890"})
    void shouldReturnFalseWhenVinNumberLengthIsInvalid(String invalidVin) {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);

        boolean result = validator.isValid(invalidVin, context);

        //then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("{car.vinNumber.length}");
    }

    @Test
    void shouldReturnFalseWhenVinNumberContainsInvalidCharacters() {
        //given
        String invalidVin = "IOQ12345678901234";

        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);

        boolean result = validator.isValid(invalidVin, context);

        //then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("{car.vinNumber.contains}");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1M2AX12340W123456", "1M2AX12349W123456"})
    void shouldReturnFalseWhenVinNumberCheckDigitIsInvalid(String invalidVin) {
        //when
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);

        boolean result = validator.isValid(invalidVin, context);

        //then
        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("{car.vinNumber.check}");
    }

}