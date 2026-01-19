package repository;

import entity.Hall;
import interfaces.HallRepositoryInterface;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HallRepository implements HallRepositoryInterface {

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
    public void addHall(String name, int totalSeats) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot add hall: database connection failed");
                return;
            }

            String sql = "INSERT INTO halls(name, total_seats) VALUES (?, ?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, totalSeats);
            ps.executeUpdate();

            System.out.println("Hall added successfully");
        } catch (Exception e) {
            System.out.println("Error adding hall: " + e.getMessage());
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

    @Override
    public List<Hall> getAllHalls() {
        List<Hall> halls = new ArrayList<>();
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get halls: database connection failed");
                return halls;
            }

            String sql = "SELECT * FROM halls ORDER BY id";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hall hall = new Hall(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("total_seats")
                );
                halls.add(hall);
            }
        } catch (Exception e) {
            System.out.println("Error getting halls: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return halls;
    }

    @Override
    public Hall getHallById(int id) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get hall: database connection failed");
                return null;
            }

            String sql = "SELECT * FROM halls WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Hall(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("total_seats")
                );
            }
        } catch (Exception e) {
            System.out.println("Error getting hall: " + e.getMessage());
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
}