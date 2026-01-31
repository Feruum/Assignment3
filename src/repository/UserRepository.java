package repository;

import entity.User;
import interfaces.UserRepositoryInterface;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements UserRepositoryInterface {

    @Override
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, booking_count) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getBookingCount());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("booking_count")
                    );
                }
            }
        }
        return null; // Not found
    }

    @Override
    public boolean userExists(String username) throws SQLException {
        return getUserByUsername(username) != null;
    }
    
    @Override
    public void updateUserBookingCount(String username, int newCount) throws SQLException {
        String sql = "UPDATE users SET booking_count = ? WHERE username = ?";
         try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
             stmt.setInt(1, newCount);
             stmt.setString(2, username);
             stmt.executeUpdate();
         }
    }
}
