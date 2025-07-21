package com._travelers.happy_travel.users.dto;

import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.Role;

public class UserMapper {

    public static User toEntity(UserRegisterRequest dto, Role role) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .role(role)
                .build();
    }

    public static UserResponse toDto(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString()
        );
    }
}
