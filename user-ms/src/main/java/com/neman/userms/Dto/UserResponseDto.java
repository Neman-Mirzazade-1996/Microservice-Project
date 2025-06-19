package com.neman.userms.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    Long id;
    String firstName;
    String lastName;
    String email;
    String username;
    String phone;
    String address;
    String city;
    String country;
    String postalCode;
    String role;
    String status;
    String profilePicture;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
