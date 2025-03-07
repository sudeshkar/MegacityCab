package com.megacitycab.model;

public enum UserRole {
	CUSTOMER, ADMIN, DRIVER;

	public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase()); // Ensure case insensitivity
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role in database: " + role);
        }
    }

    @Override
    public String toString() {
        return this.name();
    }

}
