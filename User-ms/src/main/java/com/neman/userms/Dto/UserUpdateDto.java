package com.neman.userms.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDto {
    String firstName;
    String lastName;
    String phone;
    String address;
    String city;
    String country;
    String postalCode;
    String profilePicture;
    String status;
}
