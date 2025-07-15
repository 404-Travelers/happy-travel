package com._travelers.happy_travel.destinations.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DestinationRequest(
        @NotNull (message = "Country is required")
        @Size(max=50, message = "Country must be less than 50 characters")
        String country,

        @NotNull (message = "City is required")
        @Size(max=50, message = "City must be less than 50 characters")
        String city,

        @Size(max=255, message = "Description must be less than 255 characters")
        String description,

        @Pattern(message = "Invalid content type", regexp = "^(https?://.*\\.(png|jpg|jpeg|gif|svg))$")
        String imageUrl
) {
}
