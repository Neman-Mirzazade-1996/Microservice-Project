package com.neman.userms.Service.ServiceImpl;

import com.neman.userms.Dto.auth.AuthenticationRequest;
import com.neman.userms.Dto.auth.AuthenticationResponse;
import com.neman.userms.Dto.auth.RegisterRequest;
import com.neman.userms.Exception.UserAlreadyExistException;
import com.neman.userms.Exception.UserNotFoundException;
import com.neman.userms.Model.Role;
import com.neman.userms.Model.User;
import com.neman.userms.Repository.UserRepository;
import com.neman.userms.Security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        // Check if user already exists
        if(repository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with this email already exists");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getEmail()) // using email as username
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .profilePicture(request.getProfilePicture())
                .role(Role.USER)
                .status("ACTIVE")
                .build();

        repository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Get user and generate tokens
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            log.debug("User authorities during authentication: {}", authorities);

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            log.debug("Generated token for user: {}, with role: {}", user.getEmail(), user.getRole());

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }
}
