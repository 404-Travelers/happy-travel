package com._travelers.happy_travel.destinations.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DestinationRequest(
        @Schema(description = "Country name", example = "Spain", required = true)
        @NotBlank (message = "Country is required")
        @Size(min=3, max=50, message = "Country must be more than 3 characters and less than 50 characters")
        String country,

        @Schema(description = "City name", example = "Valencia", required = true)
        @NotBlank (message = "City is required")
        @Size(min=3, max=50, message = "City must be more than 3 characters and less than 50 characters")
        String city,

        @Schema(description = "Description", example = "A beautiful city")
        @Size(max=255, message = "Description must be less than 255 characters")
        String description,

        @Schema(description = "Image", example = "https://example.com/image.jpg")
        @Pattern(message = "Invalid content type", regexp = "^(https?://.*\\.(png|jpg|jpeg|gif|svg))$")
        String image
) {
}
