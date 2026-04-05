package com.zorvyn.zorvynfinanceapi.controller;

import com.zorvyn.zorvynfinanceapi.dto.AuthRequest;
import com.zorvyn.zorvynfinanceapi.dto.AuthResponse;
import com.zorvyn.zorvynfinanceapi.entity.User;
import com.zorvyn.zorvynfinanceapi.repository.UserRepository;
import com.zorvyn.zorvynfinanceapi.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(request.username()).orElseThrow();

        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        User newUser = User.builder()
                .username(request.username())
                // In a real app, inject PasswordEncoder. For now, this is a simplified example.
                .password(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(request.password()))
                .role(com.zorvyn.zorvynfinanceapi.entity.Role.VIEWER) // Default role
                .active(true)
                .build();

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }
}