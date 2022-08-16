package com.example.car_rental.mappers;

import com.example.car_rental.dto.RentalDto;
import com.example.car_rental.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentalDtoMapper {

    RentalDtoMapper INSTANCE = Mappers.getMapper(RentalDtoMapper.class);
    Rental toEntity(RentalDto dto);
    RentalDto toDto(Rental entity);
}
