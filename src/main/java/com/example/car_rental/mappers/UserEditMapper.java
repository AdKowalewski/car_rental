package com.example.car_rental.mappers;

import com.example.car_rental.dto.UserEdit;
import com.example.car_rental.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEditMapper {

    UserEditMapper INSTANCE = Mappers.getMapper(UserEditMapper.class);
    User toEntity(UserEdit dto);
    UserEdit toDto(User entity);
}
