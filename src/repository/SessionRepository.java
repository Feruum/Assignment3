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

        String sql = """
        INSERT INTO sessions(movie_id, price, start_time)
        VALUES (?, ?, ?)
        """;

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.setDouble(2, price);
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(startTime));

            ps.executeUpdate();
            System.out.println("Session added");
        }
    }



    public List<Session> getAvailableSessions() throws java.sql.SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions ORDER BY start_time";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            sessions.add(new Session(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getDouble("price"),
                    rs.getString("start_time")
            ));
        }

        return sessions;
    }

    public Session getSessionById(int id) throws java.sql.SQLException {
        String sql = "SELECT * FROM sessions WHERE id=?";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Session(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getDouble("price"),
                    rs.getString("start_time")
            );
        }

        return null;
    }

    public List<Session> getSessionsByMovieId(int movieId) throws java.sql.SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions WHERE movie_id=? ORDER BY start_time";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setInt(1, movieId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            sessions.add(new Session(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getDouble("price"),
                    rs.getString("start_time")
            ));
        }

        return sessions;
    }
}
