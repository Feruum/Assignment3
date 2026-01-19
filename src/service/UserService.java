package service;

import entity.User;
import repository.UserRepository;

/**
 * Service for user work
 * Does login and access logic
 */
public class UserService {

    // Repository for user data work
    private UserRepository userRepository = new UserRepository();

    // Current logged in user
    private User currentUser = null;

    /**
     * Register new user in system
     * @param username user name
     * @param password user password
     * @param role user role ("admin" or "customer")
     * @return true if ok, false if error
     */
    public boolean register(String username, String password, String role) {
        // Check input data
        if (username == null || username.trim().isEmpty() || username.length() < 3) {
            System.out.println("Username must be at least 3 characters long");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Password must be at least 6 characters long");
            return false;
        }

        if (!role.equals("admin") && !role.equals("customer")) {
            System.out.println("Role must be 'admin' or 'customer'");
            return false;
        }

        // Check if name is taken
        if (userRepository.isUsernameTaken(username)) {
            System.out.println("Username already exists");
            return false;
        }

        // Add the user
        userRepository.addUser(username, password, role);
        return true;
    }

    public boolean login(String username, String password) {
        User user = userRepository.authenticate(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Welcome, " + user.getUsername() + "! Role: " + user.getRole());
            return true;
        } else {
            System.out.println("Invalid username or password");
            return false;
        }
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getUsername());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    public boolean isCustomer() {
        return currentUser != null && "customer".equals(currentUser.getRole());
    }

    // Calculate discount from booking history
    public double calculateDiscount(double basePrice) {
        if (currentUser == null) return 0.0;

        int bookings = currentUser.getBookingCount();

        if (bookings >= 10) {
            return basePrice * 0.15; // 15% discount for loyal customers
        } else if (bookings >= 5) {
            return basePrice * 0.10; // 10% discount
        } else if (bookings >= 2) {
            return basePrice * 0.05; // 5% discount
        }

        return 0.0; // No discount for new customers
    }

    // Update booking count after booking
    public void incrementBookingCount() {
        if (currentUser != null) {
            currentUser.incrementBookingCount();
            userRepository.updateBookingCount(currentUser.getId(), currentUser.getBookingCount());
        }
    }

    // Check if user can do admin work
    public boolean canManageMovies() {
        return isAdmin();
    }

    public boolean canManageSessions() {
        return isAdmin();
    }

    public boolean canBookSeats() {
        return isLoggedIn(); // Both admin and customer can book
    }
}