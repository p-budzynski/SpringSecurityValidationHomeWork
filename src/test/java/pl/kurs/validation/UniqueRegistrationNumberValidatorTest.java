package pl.kurs.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.dto.CarDto;
import pl.kurs.entity.Car;
import pl.kurs.repository.CarRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniqueRegistrationNumberValidatorTest {
    @Mock
    private CarRepository carRepositoryMock;

    @Mock
    private ConstraintValidatorContext context;

    private UniqueRegistrationNumberValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueRegistrationNumberValidator(carRepositoryMock);
    }

    @Test
    void shouldReturnTrueWhenRegistrationNumberIsNull() {
        //given
        CarDto dto = new CarDto();
        dto.setRegistrationNumber(null);

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenRegistrationNumberIsBlank() {
        //given
        CarDto dto = new CarDto();
        dto.setRegistrationNumber("     ");

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenRegistrationNumberDoesNotExistInDatabase() {
        //given
        String registrationNumber = "WA12345";
        CarDto dto = new CarDto();
        dto.setRegistrationNumber(registrationNumber);

        when(carRepositoryMock.findByRegistrationNumber(registrationNumber)).thenReturn(Optional.empty());

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenRegistrationNumberExistsButItIsTheSameCar() {
        //given
        String registrationNumber = "WA12345";
        Long carId = 1L;

        CarDto dto = new CarDto();
        dto.setId(carId);
        dto.setRegistrationNumber(registrationNumber);

        Car existingCar = new Car();
        existingCar.setId(carId);

        when(carRepositoryMock.findByRegistrationNumber(registrationNumber)).thenReturn(Optional.of(existingCar));

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenRegistrationNumberExistsAndBelongsToAnotherCar() {
        //given
        String registrationNumber = "WA12345";

        CarDto dto = new CarDto();
        dto.setId(1L);
        dto.setRegistrationNumber(registrationNumber);

        Car anotherCar = new Car();
        anotherCar.setId(2L);

        when(carRepositoryMock.findByRegistrationNumber(registrationNumber)).thenReturn(Optional.of(anotherCar));

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertFalse(result);
    }
}