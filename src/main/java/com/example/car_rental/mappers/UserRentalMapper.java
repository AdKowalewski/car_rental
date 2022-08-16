package com.example.car_rental.mappers;

import com.example.car_rental.dto.UserRental;
import com.example.car_rental.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRentalMapper {

    UserRentalMapper INSTANCE = Mappers.getMapper(UserRentalMapper.class);
    Rental toEntity(UserRental dto);
    UserRental toDto(Rental entity);
}
