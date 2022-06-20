package com.viktorsuetnov.caloriecounting.mapper;

import com.viktorsuetnov.caloriecounting.dto.UserDTO;
import com.viktorsuetnov.caloriecounting.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toDTO(User user);
}
