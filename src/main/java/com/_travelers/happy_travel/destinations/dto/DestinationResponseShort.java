package com._travelers.happy_travel.destinations.dto;

import com._travelers.happy_travel.users.dto.UserResponse;
import com._travelers.happy_travel.users.dto.UserResponseShort;

public record DestinationResponseShort(
        String country,
        String city,
        String imageUrl,
        UserResponseShort user
) {
}
