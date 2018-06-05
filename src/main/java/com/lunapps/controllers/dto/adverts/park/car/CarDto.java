package com.lunapps.controllers.dto.adverts.park.car;

import com.lunapps.models.CarBrand;
import com.lunapps.models.VehicleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class CarDto {
    @NotNull(message = "carBrand cannot be null")
    private CarBrand carBrand;

    @NotNull(message = "model cannot be null")
    @Size(min = 1, max = 50, message = "model must be between 1 and 50 characters")
    private String model;

    @NotNull(message = "vehicleType cannot be null")
    private VehicleType vehicleType;

    private Integer yearOfProduction;

    @NotNull(message = "carLength cannot be null")
    @PositiveOrZero
    private Double carLength;

    @NotNull(message = "carColor cannot be null")
    @Size(min = 1, max = 20, message = "carColor must be between 1 and 20 characters")
    private String carColor;

    @NotNull(message = "carNumber cannot be null")
    @Size(min = 1, max = 10, message = "carNumber must be between 1 and 10 characters")
    private String carNumber;

    private String carPhoto;

    private Boolean isCarDefault;
}
