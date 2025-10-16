package com.serhiishcherbakov.support.security;

import java.time.Instant;

public record UserDetails(String id, String name, String picture, String role, Instant updatedAt) {
    public boolean hasUserRole() {
        return hasRole("USER");
    }

    public boolean hasOperatorRole() {
        return hasRole("OPERATOR");
    }

    private boolean hasRole(String role) {
        return role.equalsIgnoreCase(this.role);
    }
}
