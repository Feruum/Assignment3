package repository;

import entity.Session;
import interfaces.SessionRepositoryInterface;
import util.DatabaseConnection;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SessionRepository implements SessionRepositoryInterface {

    @Override
    public boolean testConnection() {
        try (Connection c = DatabaseConnection.connect()) {
            return c != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addSession(int movieId, double price, String startTime) throws SQLException {
        // Default hall ID 1 if not provided (overloading)
        addSession(movieId, price, startTime, 1);
    }
    
    public void addSession(int movieId, double price, String startTime, int hallId) throws SQLException {
        String sql = "INSERT INTO sessions(movie_id, price, start_time, hall_id) VALUES (?, ?, ?, ?)";

        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.setDouble(2, price);
            ps.setString(3, startTime); // Storing as String per user request/schema change
            ps.setInt(4, hallId);

            ps.executeUpdate();
            System.out.println("Session added");
        }
    }

    public List<Session> getAvailableSessions() throws java.sql.SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions ORDER BY start_time";

        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getDouble("price"),
                        rs.getString("start_time"),
                        rs.getObject("hall_id") != null ? rs.getInt("hall_id") : 1 // Handle null legacy
                ));
            }
        }
        return sessions;
    }

    public Session getSessionById(int id) throws java.sql.SQLException {
        String sql = "SELECT * FROM sessions WHERE id=?";

        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Session(
                            rs.getInt("id"),
                            rs.getInt("movie_id"),
                            rs.getDouble("price"),
                            rs.getString("start_time"),
                            rs.getObject("hall_id") != null ? rs.getInt("hall_id") : 1
                    );
                }
            }
        }
        return null;
    }

    public List<Session> getSessionsByMovieId(int movieId) throws java.sql.SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions WHERE movie_id=? ORDER BY start_time";

         try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(new Session(
                            rs.getInt("id"),
                            rs.getInt("movie_id"),
                            rs.getDouble("price"),
                            rs.getString("start_time"),
                            rs.getObject("hall_id") != null ? rs.getInt("hall_id") : 1
                    ));
                }
            }
        }
        return sessions;
    }
}
