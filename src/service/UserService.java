package service;

import entity.User;
import repository.UserRepository;

public class UserService {

    private UserRepository userRepository = new UserRepository();
    private User currentUser;

    // Registration
    public boolean register(String username, String password, String role) {

        if (username == null || username.length() < 3) {
            System.out.println("Username too short");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Password too short");
            return false;
        }

        if (!role.equals("admin") && !role.equals("customer")) {
            System.out.println("Wrong role");
            return false;
        }

        if (userRepository.isUsernameTaken(username)) {
            System.out.println("Username already exists");
            return false;
        }

        userRepository.addUser(username, password, role);
        System.out.println("User registered");
        return true;
    }

    // Login
    public boolean login(String username, String password) {
        currentUser = userRepository.authenticate(username, password);

        if (currentUser == null) {
            System.out.println("Login failed");
            return false;
        }

        System.out.println("Logged in as " + currentUser.getUsername());
        return true;
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out");
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Simple discount logic
    public double calculateDiscount(double price) {
        if (currentUser == null) return 0;

        int count = currentUser.getBookingCount();

        if (count >= 10) return price * 0.15;
        if (count >= 5) return price * 0.10;
        if (count >= 2) return price * 0.05;

        return 0;
    }

    public void incrementBookingCount() {
        if (currentUser != null) {
            currentUser.incrementBookingCount();
            userRepository.updateBookingCount(
                    currentUser.getId(),
                    currentUser.getBookingCount()
            );
        }
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole().equals("admin");
    }
}
