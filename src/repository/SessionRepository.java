package repository;

import entity.Session;
import interfaces.SessionRepositoryInterface;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public void addSession(int movieId, double price, LocalDateTime startTime, int hallId) {

        // Simple business rule
        if (startTime.isBefore(LocalDateTime.now())) {
            System.out.println("Session cannot be in the past");
            return;
        }

        String sql = "INSERT INTO sessions(movie_id, price, start_time, hall_id) VALUES (?, ?, ?, ?)";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.setDouble(2, price);
            ps.setTimestamp(3, Timestamp.valueOf(startTime));
            ps.setInt(4, hallId);
            ps.executeUpdate();

            System.out.println("Session added");

        } catch (Exception e) {
            System.out.println("Error adding session: " + e.getMessage());
        }
    }

    // Sessions that have not started yet
    public List<Session> getAvailableSessions() {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions WHERE start_time > NOW() ORDER BY start_time";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getDouble("price"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getInt("hall_id")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error getting sessions: " + e.getMessage());
        }
        return sessions;
    }

    public Session getSessionById(int id) {
        String sql = "SELECT * FROM sessions WHERE id=?";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Session(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getDouble("price"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getInt("hall_id")
                );
            }

        } catch (Exception e) {
            System.out.println("Error getting session: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Session> getSessionsByMovieId(int movieId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions WHERE movie_id=? AND start_time > NOW() ORDER BY start_time";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getDouble("price"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getInt("hall_id")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error getting sessions by movie: " + e.getMessage());
        }
        return sessions;
    }
}
