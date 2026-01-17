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
public class UniqueVinNumberValidatorTest {

    @Mock
    private CarRepository carRepositoryMock;

    @Mock
    private ConstraintValidatorContext context;

    private UniqueVinNumberValidator validator;

    @BeforeEach
    void setUp() {
    validator = new UniqueVinNumberValidator(carRepositoryMock);
    }

    @Test
    void shouldReturnTrueWhenVinNumberIsNull() {
        //given
        CarDto dto = new CarDto();
        dto.setVinNumber(null);

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenVinNumberIsBlank() {
        //given
        CarDto dto = new CarDto();
        dto.setVinNumber("     ");

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenVinDoesNotExistInDatabase() {
        //given
        String vin = "UNIQUE123";
        CarDto dto = new CarDto();
        dto.setVinNumber(vin);

        when(carRepositoryMock.findByVinNumber(vin)).thenReturn(Optional.empty());

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenVinExistsButItIsTheSameCar() {
        //given
        String vin = "EXISTING123";
        Long carId = 1L;

        CarDto dto = new CarDto();
        dto.setId(carId);
        dto.setVinNumber(vin);

        Car existingCar = new Car();
        existingCar.setId(carId);

        when(carRepositoryMock.findByVinNumber(vin)).thenReturn(Optional.of(existingCar));

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenVinExistsAndBelongsToAnotherCar() {
        //given
        String vin = "EXISTING123";

        CarDto dto = new CarDto();
        dto.setId(1L);
        dto.setVinNumber(vin);

        Car anotherCar = new Car();
        anotherCar.setId(2L);

        when(carRepositoryMock.findByVinNumber(vin)).thenReturn(Optional.of(anotherCar));

        //when
        boolean result = validator.isValid(dto, context);

        //then
        assertFalse(result);
    }
}