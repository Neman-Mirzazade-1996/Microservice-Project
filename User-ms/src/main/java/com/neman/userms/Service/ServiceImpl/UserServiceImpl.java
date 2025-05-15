package com.neman.userms.Service.ServiceImpl;

import com.neman.userms.Dto.UserRequestDto;
import com.neman.userms.Dto.UserResponseDto;
import com.neman.userms.Mapper.UserMapper;
import com.neman.userms.Model.User;
import com.neman.userms.Repository.UserRepository;
import com.neman.userms.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user= userMapper.toEntity(userRequestDto);
        if (userRepository.findByEmail(userRequestDto.getEmail()) != null) {
            log.error("User with email {} already exists", user.getEmail());
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        if (userRepository.findByUsername(userRequestDto.getUsername()) != null) {
            log.error("User with username {} already exists", user.getUsername());
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }

        User user1=User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .phone(userRequestDto.getPhone())
                .address(userRequestDto.getAddress())
                .city(userRequestDto.getCity())
                .country(userRequestDto.getCountry())
                .postalCode(userRequestDto.getPostalCode())
                .role(userRequestDto.getRole())
                .build();

        return userMapper.toResponseDto(userRepository.save(user1));

    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found with given id: "+id));
        return userMapper.toResponseDto(user);

    }
}
