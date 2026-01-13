package pl.kurs.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.dto.CarDto;
import pl.kurs.entity.Car;
import pl.kurs.mapper.CarMapper;
import pl.kurs.service.CarService;
import pl.kurs.validation.Create;
import pl.kurs.validation.Update;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@Validated
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getById(@PathVariable("id") @Min(value = 1, message = "{car.controller.id.min}") Long id) {
        Car car = carService.getCarById(id);
        return ResponseEntity.ok(carMapper.entityToDto(car));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDto createCar(@RequestBody @Validated(Create.class) CarDto carDto) {
        Car car = carMapper.dtoToEntity(carDto);
        Car savedCar = carService.saveCar(car);
        return carMapper.entityToDto(savedCar);
    }

    @PutMapping
    public ResponseEntity<CarDto> updateCar(@RequestBody @Validated(Update.class) CarDto carDto) {
        Car car = carMapper.dtoToEntityWithId(carDto);
        Car updatedCar = carService.updatedCar(car);
        return ResponseEntity.ok(carMapper.entityToDto(updatedCar));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Min(value = 1, message = "{car.controller.id.min}") Long id) {
        carService.deleteCarById(id);
    }
}
