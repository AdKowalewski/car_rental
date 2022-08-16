package com.example.car_rental.mappers;

import com.example.car_rental.dto.UserDto;
import com.example.car_rental.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDtoMapper {

    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);
    User toEntity(UserDto dto);
    UserDto toDto(User entity);
}
