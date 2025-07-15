package com._travelers.happy_travel.destinations.dto;

import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.users.User;

public class DestinationMapper {
    public static Destination toEntity(DestinationRequest dto, User user){
        return Destination.builder()
                .country(dto.country())
                .city(dto.city())
                .description(dto.description())
                .imageUrl(dto.imageUrl())
                .user(user)
                .build();
    }
    public static DestinationResponse toDto(Destination destination, User user){
        return new DestinationResponse(destination.getCountry(),destination.getCity(), destination.getDescription(), destination.getImageUrl(), destination.getUser());
    }
    public static DestinationResponseShort toDtoShort (Destination destination, User user){
        return new DestinationResponseShort(destination.getCountry(),destination.getCity(), destination.getImageUrl(), destination.getUser());
    }
}
