package com.neman.userms.Controller;

import com.neman.userms.Dto.UserRequestDto;
import com.neman.userms.Dto.UserResponseDto;
import com.neman.userms.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @PostMapping("/admin/create")
    @Operation(
        summary = "Create new user (Admin only)",
        description = "Allows administrators to create users with any role. For regular user registration, use /api/v1/auth/register"
    )
    public ResponseEntity<UserResponseDto> createUserByAdmin(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Operation(summary = "Create new user")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // TODO: Implement delete method in service
        return ResponseEntity.ok().build();
    }


}
