package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pl.kurs.dto.CarDto;
import pl.kurs.repository.CarRepository;

@RequiredArgsConstructor
public class UniqueVinNumberValidator implements ConstraintValidator<UniqueVinNumber, CarDto> {
    private final CarRepository carRepository;

    @Override
    public boolean isValid(CarDto carDto, ConstraintValidatorContext context) {
        String vinNumber = carDto.getVinNumber();

        if (vinNumber == null || vinNumber.isBlank()) return true;

        return carRepository.findByVinNumber(vinNumber)
                .map(car -> car.getId().equals(carDto.getId()))
                .orElse(true);
    }

}
