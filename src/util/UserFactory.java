package util;

import entity.User;

public class UserFactory {

    public static User createUser(String username, String password, String role) {
        if (!ValidationUtils.isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (!ValidationUtils.isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password (min length 4)");
        }
        
        // Default to customer if invalid role
        String finalRole = "customer";
        if (role != null && role.equalsIgnoreCase("admin")) {
            finalRole = "admin";
        }
        
        return new User(username, password, finalRole);
    }
}
