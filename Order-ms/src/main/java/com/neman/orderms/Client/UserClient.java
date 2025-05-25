package com.neman.orderms.Client;

import com.neman.orderms.Dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface  UserClient {

    @GetMapping("/api/v1/users/getUser/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
}
