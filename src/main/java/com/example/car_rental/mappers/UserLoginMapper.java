package com.example.car_rental.mappers;

import com.example.car_rental.dto.UserLogin;
import com.example.car_rental.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserLoginMapper {

    UserLoginMapper INSTANCE = Mappers.getMapper(UserLoginMapper.class);
    User toEntity(UserLogin dto);
    UserLogin toDto(User entity);
}
