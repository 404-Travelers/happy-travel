package com._travelers.happy_travel.destinations.dto;

import com._travelers.happy_travel.users.User;

public record DestinationResponse(
        String country,
        String city,
        String description,
        String imageUrl,
        User user
) {
}
