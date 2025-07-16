package com._travelers.happy_travel.users.dto;

import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.Role;

public class UserMapper {

    public static User toEntity(UserRegisterRequest dto) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .role(Role.USER)
                .build();
    }

    public static UserResponse toDto(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
