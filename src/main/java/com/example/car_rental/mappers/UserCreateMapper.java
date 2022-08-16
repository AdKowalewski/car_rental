package com.example.car_rental.mappers;

import com.example.car_rental.dto.UserCreate;
import com.example.car_rental.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserCreateMapper {

    UserCreateMapper INSTANCE = Mappers.getMapper(UserCreateMapper.class);
    User toEntity(UserCreate dto);
    UserCreate toDto(User entity);
}
