package com.neman.userms.Controller;

import com.neman.userms.Dto.UserDto;
import com.neman.userms.Dto.UserRequestDto;
import com.neman.userms.Dto.UserResponseDto;
import com.neman.userms.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/createUser")
    public UserResponseDto addUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/getUser/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
