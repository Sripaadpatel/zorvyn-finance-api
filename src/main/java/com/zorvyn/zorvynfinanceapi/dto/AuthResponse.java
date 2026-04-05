package com.zorvyn.zorvynfinanceapi.dto;

public record AuthResponse(
        String token,
        String username,
        String role
) {}