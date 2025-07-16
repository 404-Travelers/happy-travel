package com._travelers.happy_travel.users.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginRequest(

        @NotEmpty(message = "Username is required")
        String username,

        @NotEmpty(message = "Password is required")
        String password

) {}