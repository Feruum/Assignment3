package repository;

import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BookingRepository {

    public boolean isSeatFree(int sessionId, int seat) {
        try {
            Connection c = DatabaseConnection.connect();
            Statement s = c.createStatement();

            String sql = "SELECT * FROM bookings WHERE session_id="
                    + sessionId + " AND seat_number=" + seat;

            ResultSet rs = s.executeQuery(sql);
            boolean free = !rs.next();
            c.close();
            return free;
        } catch (Exception e) {
            return false;
        }
    }

    public void book(int sessionId, int seat, String name) {
        try {
            Connection c = DatabaseConnection.connect();
            Statement s = c.createStatement();

            String sql = "INSERT INTO bookings(session_id, seat_number, customer) VALUES ("
                    + sessionId + "," + seat + ",'" + name + "')";
            s.executeUpdate(sql);

            System.out.println("Seat booked");
            c.close();
        } catch (Exception e) {
            System.out.println("Booking error");
        }
    }
}