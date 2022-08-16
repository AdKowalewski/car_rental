package com.example.car_rental.mappers;

import com.example.car_rental.dto.CarCreate;
import com.example.car_rental.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarCreateMapper {

    CarCreateMapper INSTANCE = Mappers.getMapper(CarCreateMapper.class);
    Car toEntity(CarCreate dto);
    CarCreate toDto(Car entity);
}
