package repository;

import entity.Hall;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HallRepository {

    public Hall getHallById(int id) throws SQLException {
        String sql = "SELECT * FROM halls WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Hall(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("total_seats")
                    );
                }
            }
        }
        return null;
    }
}
