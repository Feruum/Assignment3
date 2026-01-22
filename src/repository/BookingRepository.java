package repository;

import entity.Booking;
import interfaces.BookingRepositoryInterface;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository implements BookingRepositoryInterface {


    public boolean testConnection() {
        try (Connection c = DatabaseConnection.connect()) {
            return c != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSeatFree(int sessionId, int seatNumber) throws java.sql.SQLException {
        String sql = "SELECT * FROM bookings WHERE session_id=? AND seat_number=?";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setInt(1, sessionId);
        ps.setInt(2, seatNumber);

        ResultSet rs = ps.executeQuery();
        return !rs.next(); // если записи нет — место свободно
    }

    public void book(int sessionId, int seat, String name) throws java.sql.SQLException {
        String sql = "INSERT INTO bookings(session_id, seat_number, customer) VALUES (?, ?, ?)";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setInt(1, sessionId);
        ps.setInt(2, seat);
        ps.setString(3, name);

        ps.executeUpdate();
        System.out.println("Seat booked");
    }

    public List<Booking> getBookingsBySessionId(int sessionId) throws java.sql.SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE session_id=?";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setInt(1, sessionId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Booking(
                    rs.getInt("session_id"),
                    rs.getInt("seat_number"),
                    rs.getString("customer")
            ));
        }

        return list;
    }
    public List<Booking> getBookingsByCustomer(String customerName) throws java.sql.SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customer=?";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setString(1, customerName);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Booking(
                    rs.getInt("session_id"),
                    rs.getInt("seat_number"),
                    rs.getString("customer")
            ));
        }

        return list;
    }

    @Override
    public boolean cancelBooking(int sessionId, int seatNumber, String customerName) throws java.sql.SQLException {
        String sql = "DELETE FROM bookings WHERE session_id=? AND seat_number=? AND customer=?";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setInt(1, sessionId);
        ps.setInt(2, seatNumber);
        ps.setString(3, customerName);

        return ps.executeUpdate() > 0;
    }
}
