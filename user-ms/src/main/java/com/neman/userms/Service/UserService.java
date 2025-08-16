package com.neman.userms.Service;

import com.neman.userms.Dto.UserRequestDto;
import com.neman.userms.Dto.UserResponseDto;
import com.neman.userms.Model.User;


public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long id);


    // Spring-cloud authentication üçün User entity qaytaran metod
    User findUserEntityByEmail(String email);
}
