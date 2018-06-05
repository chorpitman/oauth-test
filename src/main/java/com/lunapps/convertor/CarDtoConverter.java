package com.lunapps.convertor;

import com.lunapps.controllers.dto.adverts.park.car.CarDto;
import com.lunapps.controllers.dto.adverts.park.car.CarUpdateDto;
import com.lunapps.exception.user.UserException;
import com.lunapps.models.Car;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;

@Component
public class CarDtoConverter {
    public Car convert(CarDto dto) {
        if (Objects.isNull(dto)) throw new UserException(MessageFormat.format("user car {0} can not be null", dto));
        Car car = new Car();
        car.setCarBrand(dto.getCarBrand());
        car.setModel(dto.getModel().toUpperCase());
        car.setVehicleType(dto.getVehicleType());
        car.setYearOfProduction(dto.getYearOfProduction());
        car.setCarLength(dto.getCarLength());
        car.setCarColor(dto.getCarColor().toUpperCase());
        car.setCarNumber(dto.getCarNumber());
        car.setCarPhoto(dto.getCarPhoto());
        car.setIsCarDefault(dto.getIsCarDefault());
        return car;
    }

    public Car convert(CarUpdateDto dto) {
        if (Objects.isNull(dto)) throw new UserException(MessageFormat.format("user car {0} can not be null", dto));
        Car car = new Car();
        car.setId(dto.getId());
        car.setCarBrand(dto.getCarBrand());
        car.setModel(dto.getModel());
        car.setVehicleType(dto.getVehicleType());
        car.setYearOfProduction(dto.getYearOfProduction());
        car.setCarLength(dto.getCarLength());
        car.setCarColor(dto.getCarColor().toUpperCase());
        car.setCarNumber(dto.getCarNumber());
        car.setIsCarDefault(dto.getIsCarDefault());

        return car;
    }


    public CarUpdateDto convert(Car car) {
        if (Objects.isNull(car)) throw new UserException(MessageFormat.format("user car {0} can not be null", car));
        CarUpdateDto carUpdateDto = new CarUpdateDto();
        carUpdateDto.setId(car.getId());
        carUpdateDto.setCarBrand(car.getCarBrand());
        carUpdateDto.setModel(car.getModel());
        carUpdateDto.setVehicleType(car.getVehicleType());
        carUpdateDto.setYearOfProduction(car.getYearOfProduction());
        carUpdateDto.setCarLength(car.getCarLength());
        carUpdateDto.setCarColor(car.getCarColor());
        carUpdateDto.setCarNumber(car.getCarNumber());
        carUpdateDto.setIsCarDefault(car.getIsCarDefault());
        carUpdateDto.setCarPhoto(car.getCarPhoto());

        return carUpdateDto;
    }
}
