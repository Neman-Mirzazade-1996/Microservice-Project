package com.neman.userms.Config;

import com.neman.userms.Dto.UserRequestDto;
import com.neman.userms.Model.User;
import com.neman.userms.Repository.UserRepository;
import com.neman.userms.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        // Check if admin user exists
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            try {
                UserRequestDto adminUser = new UserRequestDto();
                adminUser.setFirstName("Admin");
                adminUser.setLastName("Admin");
                adminUser.setEmail("admin@gmail.com");
                adminUser.setPassword("admin123");
                adminUser.setRole("ADMIN");

                userService.createUser(adminUser);
                log.info("Admin user created successfully");
            } catch (Exception e) {
                log.error("Could not create admin user. Error: {}", e.getMessage());
            }
        } else {
            log.info("Admin user already exists. Skipping creation.");
        }
    }
}
