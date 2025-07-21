package com._travelers.happy_travel.users;

import lombok.ToString;


public enum Role {
    USER,
    ADMIN;


    @Override
    public String toString() {
        return "ROLE_ " + this.name();
    }

}
