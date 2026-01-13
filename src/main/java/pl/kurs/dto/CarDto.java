package pl.kurs.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.validation.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@UniqueRegistrationNumber(groups = {Create.class, Update.class})
@UniqueVinNumber(groups = {Create.class, Update.class})
public class CarDto {
    @NotNull(message = "{car.id.notNull}", groups = Update.class)
    @Min(value = 1, message = "{car.id.min}", groups = Update.class)
    private Long id;

    @NotBlank(message = "{car.producer.notBlank}", groups = {Create.class, Update.class})
    private String producer;

    @NotBlank(message = "{car.model.notBlank}", groups = {Create.class, Update.class})
    private String model;

    @NotNull(message = "{car.vinNumber.notNull}", groups = {Create.class, Update.class})
    @VinNumber(groups = {Create.class, Update.class})
    private String vinNumber;

    @NotNull(message = "{car.yearOfProduction.notNull}", groups = {Create.class, Update.class})
    @YearInRange(groups = {Create.class, Update.class})
    private Integer yearOfProduction;

    @NotBlank(message = "{car.registrationNumber.notBlank}", groups = {Create.class, Update.class})
    @ValidPolishPlate(groups = {Create.class, Update.class})
    private String registrationNumber;
}
