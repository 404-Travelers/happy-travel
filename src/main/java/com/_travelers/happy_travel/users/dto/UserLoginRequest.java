package com._travelers.happy_travel.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(

        @Schema(description = "Username", example = "johndoe", required = true)
        @NotBlank(message = "Username is required")
        @Size(max = 50, message = "Username must be less than 50 characters")
        String username,

        @Schema(description = "Password", example = "password123", required = true)
        @NotBlank(message = "Password is required")
        @Size(max = 50, message = "Password must be less than 50 characters")
        String password

) {}