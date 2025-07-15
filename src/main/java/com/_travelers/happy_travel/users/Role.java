package com._travelers.happy_travel.users;

import lombok.ToString;


public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    Role(String name){
        this.name = name;
    }
}
