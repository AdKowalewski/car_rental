package com.example.car_rental.mappers;

import com.example.car_rental.dto.CarDto;
import com.example.car_rental.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarDtoMapper {

    CarDtoMapper INSTANCE = Mappers.getMapper(CarDtoMapper.class);
    Car toEntity(CarDto dto);
    CarDto toDto(Car entity);
}
