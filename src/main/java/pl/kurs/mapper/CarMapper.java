package pl.kurs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kurs.dto.CarDto;
import pl.kurs.entity.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "id", ignore = true)
    Car dtoToEntity (CarDto dto);

    Car dtoToEntityWithId(CarDto carDto);

    CarDto entityToDto (Car car);
}
