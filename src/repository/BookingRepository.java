package repository;
import java.sql.Statement;
import entity.Booking;
import interfaces.BookingRepositoryInterface;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository implements BookingRepositoryInterface {

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
    public boolean isSeatFree(int sessionId, int seatNumber) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot check seat availability: database connection failed");
                return false;
            }

            Statement s = c.createStatement();

            String sql = "SELECT * FROM bookings WHERE session_id="
                    + sessionId + " AND seat_number=" + seatNumber;

            ResultSet rs = s.executeQuery(sql);
            boolean free = !rs.next();
            return free;
        } catch (Exception e) {
            System.out.println("Error checking seat availability: " + e.getMessage());
            return false;
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

    public void book(int sessionId, int seat, String name) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot book seat: database connection failed");
                return;
            }

            Statement s = c.createStatement();

            String sql = "INSERT INTO bookings(session_id, seat_number, customer) VALUES ("
                    + sessionId + "," + seat + ",'" + name + "')";
            s.executeUpdate(sql);

            System.out.println("Seat booked");
        } catch (Exception e) {
            System.out.println("Booking error: " + e.getMessage());
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
    public List<Booking> getBookingsBySessionId(int sessionId) {
        List<Booking> bookings = new ArrayList<>();
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get bookings: database connection failed");
                return bookings;
            }

            String sql = "SELECT * FROM bookings WHERE session_id=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, sessionId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("session_id"),
                    rs.getInt("seat_number"),
                    rs.getString("customer")
                );
                bookings.add(booking);
            }
        } catch (Exception e) {
            System.out.println("Error getting bookings: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByCustomer(String customerName) {
        List<Booking> bookings = new ArrayList<>();
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get customer bookings: database connection failed");
                return bookings;
            }

            String sql = "SELECT * FROM bookings WHERE customer=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, customerName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("session_id"),
                    rs.getInt("seat_number"),
                    rs.getString("customer")
                );
                bookings.add(booking);
            }
        } catch (Exception e) {
            System.out.println("Error getting customer bookings: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return bookings;
    }

    @Override
    public boolean cancelBooking(int sessionId, int seatNumber, String customerName) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot cancel booking: database connection failed");
                return false;
            }

            String sql = "DELETE FROM bookings WHERE session_id=? AND seat_number=? AND customer=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, sessionId);
            ps.setInt(2, seatNumber);
            ps.setString(3, customerName);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Error canceling booking: " + e.getMessage());
            return false;
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