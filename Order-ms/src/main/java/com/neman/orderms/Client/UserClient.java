package com.neman.orderms.Client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface  UserClient {
    @GetMapping("/users/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
}
