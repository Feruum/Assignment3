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
        try {
            Connection c = DatabaseConnection.connect();
            if (c != null) {
                c.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addUser(String username, String password, String role) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot register user: database connection failed");
                return;
            }

            String sql = "INSERT INTO users(username, password, role, booking_count) VALUES (?, ?, ?, 0)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();

            System.out.println("User registered successfully");
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
    }

    public User authenticate(String username, String password) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot authenticate: database connection failed");
                return null;
            }

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getInt("booking_count")
                );
                return user;
            }
        } catch (Exception e) {
            System.out.println("Authentication error: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return null;
    }

    public void updateBookingCount(int userId, int newCount) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot update booking count: database connection failed");
                return;
            }

            String sql = "UPDATE users SET booking_count=? WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, newCount);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updating booking count: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
    }

    public boolean isUsernameTaken(String username) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot check username: database connection failed");
                return false; // Assume not taken if we can't check
            }

            String sql = "SELECT id FROM users WHERE username=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            boolean taken = rs.next();
            return taken;
        } catch (Exception e) {
            System.out.println("Error checking username: " + e.getMessage());
            return false; // Don't block registration due to DB errors
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
    }
}