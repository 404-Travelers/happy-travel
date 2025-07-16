package com._travelers.happy_travel.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public record UserResponse (
    String username,
    String email,
    String role
) {

}
