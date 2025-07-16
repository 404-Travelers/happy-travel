package com._travelers.happy_travel.users.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record UserRegisterRequest(
    @NotEmpty(message = "Username is required")
    @Size(min = 5, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    String email,

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password
) {
}
