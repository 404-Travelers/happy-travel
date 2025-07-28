package com._travelers.happy_travel.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UserRegisterRequest(
    @Schema(description = "Username", example = "marrio", required = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @Schema(description = "Email", example = "marrio@example.com", required = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    @Email(message = "Email not valid", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    String email,

    @Schema(description = "Password", example = "Password_123?", required = true)
    @NotBlank(message = "Password is required")
    @Pattern(message = "Password must contain a minimum of 8 characters and a max of 50 characters, including a number, one uppercase letter, one lowercase letter and one special character", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,50}$")
    String password
) {
}
