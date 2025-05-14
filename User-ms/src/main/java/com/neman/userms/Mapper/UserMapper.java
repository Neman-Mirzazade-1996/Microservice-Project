package com.neman.userms.Mapper;

import com.neman.userms.Dto.*;
import com.neman.userms.Model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);
    UserResponseDto toResponseDto(User user);
    User toEntity(UserUpdateDto dto);
    User toEntity(UserLoginRequestDto dto);

    UserDto toUserDto(User user); 
    User toUser(UserDto dto);

    List<UserResponseDto> toResponseDtoList(List<User> users);
}
