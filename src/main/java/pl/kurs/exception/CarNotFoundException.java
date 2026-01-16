package pl.kurs.exception;

import lombok.Getter;

@Getter
public class CarNotFoundException extends RuntimeException {
    private final Long carId;
    public CarNotFoundException(Long carId) {
        super();
        this.carId = carId;
    }
}
