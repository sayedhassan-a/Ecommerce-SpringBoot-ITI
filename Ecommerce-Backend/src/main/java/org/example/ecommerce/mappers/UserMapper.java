package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.UserDto;
import org.example.ecommerce.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<User, UserDto> {
}