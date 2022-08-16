package com.example.car_rental.mappers;

import com.example.car_rental.dto.CarEdit;
import com.example.car_rental.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarEditMapper {

    CarEditMapper INSTANCE = Mappers.getMapper(CarEditMapper.class);
    Car toEntity(CarEdit dto);
    CarEdit toDto(Car entity);
}
