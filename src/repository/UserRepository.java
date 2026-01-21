package repository;

import entity.User;
import interfaces.UserRepositoryInterface;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository implements UserRepositoryInterface {

    @Override
    public boolean testConnection() {
        try (Connection c = DatabaseConnection.connect()) {
            return c != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, role, booking_count) VALUES (?, ?, ?, 0)";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();

            System.out.println("User registered");

        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("booking_count")
                );
            }

        } catch (Exception e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
        return null;
    }

    public void updateBookingCount(int userId, int newCount) {
        String sql = "UPDATE users SET booking_count=? WHERE id=?";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, newCount);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error updating booking count: " + e.getMessage());
        }
    }

    public boolean isUsernameTaken(String username) {
        String sql = "SELECT id FROM users WHERE username=?";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            System.out.println("Error checking username: " + e.getMessage());
            return false;
        }
    }
}
