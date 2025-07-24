package com._travelers.happy_travel.users.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record UserRegisterRequest(
    @NotEmpty(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    @Email(message = "Email not valid", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    String email,

    @NotEmpty(message = "Password is required")
    @Pattern(message = "Password must contain a minimum of 8 characters and a max of 50 characters, including a number, one uppercase letter, one lowercase letter and one special character", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,50}$")
    String password
) {
}
