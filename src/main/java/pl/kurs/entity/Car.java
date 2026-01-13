package pl.kurs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String producer;

    @Column(nullable = false)
    private String model;

    @Column(name = "vin_number", length = 17, nullable = false, unique = true)
    private String vinNumber;

    @Column(name = "year_of_production", nullable = false)
    private Integer yearOfProduction;

    @Column(name = "registration_number", length = 10, nullable = false, unique = true)
    private String registrationNumber;

    public Car(String producer, String model, String vinNumber, Integer yearOfProduction, String registrationNumber) {
        this.producer = producer;
        this.model = model;
        this.vinNumber = vinNumber;
        this.yearOfProduction = yearOfProduction;
        this.registrationNumber = registrationNumber;
    }
}
