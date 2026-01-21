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
        try (Connection c = DatabaseConnection.connect()) {
            return c != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addHall(String name, int totalSeats) {
        String sql = "INSERT INTO halls(name, total_seats) VALUES (?, ?)";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, totalSeats);
            ps.executeUpdate();

            System.out.println("Hall added");

        } catch (Exception e) {
            System.out.println("Error adding hall: " + e.getMessage());
        }
    }

    @Override
    public List<Hall> getAllHalls() {
        List<Hall> halls = new ArrayList<>();
        String sql = "SELECT * FROM halls ORDER BY id";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                halls.add(new Hall(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("total_seats")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error getting halls: " + e.getMessage());
        }
        return halls;
    }

    @Override
    public Hall getHallById(int id) {
        String sql = "SELECT * FROM halls WHERE id=?";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

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
        }
        return null;
    }
}
