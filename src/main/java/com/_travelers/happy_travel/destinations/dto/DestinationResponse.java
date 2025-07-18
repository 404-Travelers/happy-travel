package com._travelers.happy_travel.destinations.dto;

import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.dto.UserResponse;

public record DestinationResponse(
        String country,
        String city,
        String description,
        String imageUrl,
        UserResponse user
) {
}
