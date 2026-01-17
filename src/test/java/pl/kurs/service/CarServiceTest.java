package pl.kurs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.entity.Car;
import pl.kurs.exception.CarNotFoundException;
import pl.kurs.repository.CarRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepositoryMock;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car("TestProducer", "TestModel", "TestVIN", 1999, "TestPlate");
        car.setId(1L);
    }

    @Test
    void shouldReturnCarWhenIdExists() {
        //given
        when(carRepositoryMock.findById(1L)).thenReturn(Optional.of(car));

        //when
        Car result = carService.getCarById(1L);

        //then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(carRepositoryMock).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCarNotFound() {
        //given
        when(carRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> carService.getCarById(1L))
                .isInstanceOf(CarNotFoundException.class)
                .hasFieldOrPropertyWithValue("carId", 1L);
    }

    @Test
    void shouldSaveCarSuccessfully() {
        //given
        when(carRepositoryMock.save(any(Car.class))).thenReturn(car);

        //when
        Car savedCar = carService.saveCar(car);

        //then
        assertNotNull(savedCar);
        verify(carRepositoryMock).save(car);
    }

    @Test
    void shouldDeleteCarSuccessfully() {
        //when
        carService.deleteCarById(1L);

        //then
        verify(carRepositoryMock).deleteById(1L);
    }

    @Test
    void shouldUpdateCarSuccessfully() {
        //given
        Car existingCar = new Car();
        existingCar.setId(1L);
        existingCar.setProducer("Old Producer");

        Car updatedInfo = new Car();
        updatedInfo.setId(1L);
        updatedInfo.setProducer("New Producer");

        when(carRepositoryMock.findById(1L)).thenReturn(Optional.of(existingCar));
        when(carRepositoryMock.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Car result = carService.updateCar(updatedInfo);

        //then
        assertEquals("New Producer", result.getProducer());
        verify(carRepositoryMock).save(existingCar);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentCar() {
        //given
        Car carToUpdate = new Car();
        carToUpdate.setId(999L);
        carToUpdate.setProducer("New Producer");

        when(carRepositoryMock.findById(999L)).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> carService.updateCar(carToUpdate))
                .isInstanceOf(CarNotFoundException.class)
                .hasFieldOrPropertyWithValue("carId", 999L);

        verify(carRepositoryMock, never()).save(any(Car.class));
        verify(carRepositoryMock).findById(999L);
    }
}