package com._travelers.happy_travel.destinations.dto;

import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.dto.UserMapper;
import com._travelers.happy_travel.users.dto.UserResponseShort;

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
    public static DestinationResponse toDto(Destination destination){
        UserResponseShort userDto = UserMapper.toDtoShort(destination.getUser());
        return new DestinationResponse(destination.getId(), destination.getCountry(), destination.getCity(), destination.getDescription(), destination.getImageUrl(), userDto);
    }
    public static DestinationResponseShort toDtoShort (Destination destination){
        UserResponseShort userDto = UserMapper.toDtoShort(destination.getUser());
        return new DestinationResponseShort(destination.getId(), destination.getCountry(), destination.getCity(), destination.getImageUrl(), userDto);
    }
}
