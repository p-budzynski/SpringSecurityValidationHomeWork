package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pl.kurs.dto.CarDto;
import pl.kurs.repository.CarRepository;

@RequiredArgsConstructor
public class UniqueRegistrationNumberValidator implements ConstraintValidator<UniqueRegistrationNumber, CarDto> {
    private final CarRepository carRepository;

    @Override
    public boolean isValid(CarDto carDto, ConstraintValidatorContext context) {
        String registrationNumber = carDto.getRegistrationNumber();

        if (registrationNumber == null || registrationNumber.isBlank()) return true;

        return carRepository.findByRegistrationNumber(registrationNumber)
                .map(car -> car.getId().equals(carDto.getId()))
                .orElse(true);
    }

}
