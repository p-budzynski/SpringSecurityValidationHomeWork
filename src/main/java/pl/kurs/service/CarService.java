package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.entity.Car;
import pl.kurs.exception.CarNotFoundException;
import pl.kurs.repository.CarRepository;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with id: " + id + " not found"));
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public Car updatedCar(Car car) {
        Car carToUpdate = carRepository.findById(car.getId())
                .orElseThrow(() -> new CarNotFoundException("Car with id: " + car.getId() + " not found"));
        BeanUtils.copyProperties(car, carToUpdate);
        return carRepository.save(carToUpdate);
    }
}
