package com.vendorshield.controller;

import com.vendorshield.domain.model.User;
import com.vendorshield.dto.request.LoginRequest;
import com.vendorshield.dto.response.ApiResponse;
import com.vendorshield.dto.response.AuthResponse;
import com.vendorshield.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        AuthResponse authResponse = new AuthResponse(token, "Bearer", user.getUsername(), user.getRole());
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authResponse));
    }
}