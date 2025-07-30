package com._travelers.happy_travel.destinations.dto;

import com._travelers.happy_travel.users.dto.UserResponseShort;

public record DestinationResponseShort(
        Long id,
        String country,
        String city,
        String image,
        UserResponseShort user
) {
}
