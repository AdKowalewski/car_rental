package com.example.car_rental.mappers;

import com.example.car_rental.dto.RentalCreate;
import com.example.car_rental.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentalCreateMapper {

    RentalCreateMapper INSTANCE = Mappers.getMapper(RentalCreateMapper.class);
    Rental toEntity(RentalCreate dto);
    RentalCreate toDto(Rental entity);
}
