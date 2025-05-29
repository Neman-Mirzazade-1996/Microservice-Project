package com.neman.orderms.Client;

import com.neman.orderms.Dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/v1/users", url = "http://localhost:9090")
public interface  UserClient {

    @GetMapping("/getUser/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
}
