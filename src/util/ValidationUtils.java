package util;

public class ValidationUtils {

    // Validates username (not empty, length > 3)
    public static boolean isValidUsername(String username) {
        return username != null && username.trim().length() > 3;
    }

    // Validates password (not empty, length >= 4)
    public static boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= 4;
    }

    // Validates price (must be positive)
    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    // Validates duration (must be positive)
    public static boolean isValidDuration(int duration) {
        return duration > 0;
    }
    
    // Validates role
    public static boolean isValidRole(String role) {
        return role != null && (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("customer"));
    }
}
