package pl.kurs.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.kurs.dto.CarDto;
import pl.kurs.entity.Car;

import static org.assertj.core.api.Assertions.assertThat;

public class CarMapperTest {
    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @Test
    void shouldMapDtoToEntityWithoutId() {
        //given
        Car testCar = createTestCar();
        CarDto testCarDto = createTestDtoCar();

        //when
        Car car = carMapper.dtoToEntity(testCarDto);

        //then
        assertThat(car)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testCar);
    }

    @Test
    void shouldMapDtoToEntityWithId() {
        //given
        CarDto testCarDto = createTestDtoCar();
        Car testCar = createTestCar();
        testCar.setId(testCarDto.getId());

        //when
        Car car = carMapper.dtoToEntityWithId(testCarDto);

        //then
        assertThat(car)
                .usingRecursiveComparison()
                .isEqualTo(testCar);
    }

    @Test
    void shouldMapEntityToDto() {
        //given
        CarDto testCarDto = createTestDtoCar();
        Car testCar = createTestCar();
        testCar.setId(testCarDto.getId());

        //when
        CarDto carDto = carMapper.entityToDto(testCar);

        //then
        assertThat(carDto)
                .usingRecursiveComparison()
                .isEqualTo(testCarDto);
    }

    @Test
    void shouldReturnNullWhenEntityToDtoGivenNull() {
        //when then
        assertThat(carMapper.entityToDto(null)).isNull();
    }

    @Test
    void shouldReturnNullWhenEntityToDtoWithIdGivenNull() {
        //when then
        assertThat(carMapper.dtoToEntityWithId(null)).isNull();
    }

    @Test
    void shouldReturnNullWhenDtoToEntityGivenNull() {
        //when then
        assertThat(carMapper.dtoToEntity(null)).isNull();
    }

    private Car createTestCar() {
        return new Car("TestProducer", "TestModel", "TestVIN", 2000, "TestPlate");
    }

    private CarDto createTestDtoCar() {
        return new CarDto(1L, "TestProducer", "TestModel", "TestVIN", 2000, "TestPlate");
    }
}