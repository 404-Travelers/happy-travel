package com._travelers.happy_travel.users.dto;

public record UserResponse (
    Long id,
    String username,
    String email,
    String role
) {

}
