package com.neman.userms.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    String firstName;
    String lastName;
    String email;
    String username;
    String password;
    String phone;
    String address;
    String city;
    String country;
    String postalCode;
    String role;
}
