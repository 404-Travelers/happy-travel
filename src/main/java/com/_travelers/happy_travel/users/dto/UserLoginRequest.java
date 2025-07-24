package com._travelers.happy_travel.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(

        @NotBlank(message = "Username is required")
        @Size(max = 50, message = "Username must be less than 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(max = 50, message = "Password must be less than 50 characters")
        String password

) {}