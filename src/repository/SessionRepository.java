package repository;

import entity.Session;
import interfaces.SessionRepositoryInterface;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionRepository implements SessionRepositoryInterface {

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
    public void addSession(int movieId, double price, LocalDateTime startTime, int hallId) {
        // Check if session time is valid (not past)
        if (startTime.isBefore(LocalDateTime.now())) {
            System.out.println("Cannot create session in the past");
            return;
        }

        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot add session: database connection failed");
                return;
            }

            String sql = "INSERT INTO sessions(movie_id, price, start_time, hall_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, movieId);
            ps.setDouble(2, price);
            ps.setTimestamp(3, Timestamp.valueOf(startTime));
            ps.setInt(4, hallId);
            ps.executeUpdate();

            System.out.println("Session added successfully");
        } catch (Exception e) {
            System.out.println("Error adding session: " + e.getMessage());
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

    // Get available sessions (not started yet)
    public List<Session> getAvailableSessions() {
        List<Session> sessions = new ArrayList<>();
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get sessions: database connection failed");
                return sessions;
            }

            String sql = "SELECT * FROM sessions WHERE start_time > NOW() ORDER BY start_time";
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                Session session = new Session(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getDouble("price"),
                    rs.getTimestamp("start_time").toLocalDateTime(),
                    rs.getInt("hall_id")
                );
                sessions.add(session);
            }
        } catch (Exception e) {
            System.out.println("Error getting sessions: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return sessions;
    }

    public Session getSessionById(int id) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get session: database connection failed");
                return null;
            }

            String sql = "SELECT * FROM sessions WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Session session = new Session(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getDouble("price"),
                    rs.getTimestamp("start_time").toLocalDateTime(),
                    rs.getInt("hall_id")
                );
                return session;
            }
        } catch (Exception e) {
            System.out.println("Error getting session: " + e.getMessage());
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

    @Override
    public List<Session> getSessionsByMovieId(int movieId) {
        List<Session> sessions = new ArrayList<>();
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get sessions by movie: database connection failed");
                return sessions;
            }

            String sql = "SELECT * FROM sessions WHERE movie_id=? AND start_time > NOW() ORDER BY start_time";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, movieId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Session session = new Session(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getDouble("price"),
                    rs.getTimestamp("start_time").toLocalDateTime(),
                    rs.getInt("hall_id")
                );
                sessions.add(session);
            }
        } catch (Exception e) {
            System.out.println("Error getting sessions by movie: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return sessions;
    }
}
