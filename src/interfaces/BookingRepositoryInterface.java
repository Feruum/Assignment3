package interfaces;

public interface BookingRepositoryInterface {
    void book(int sessionId, int seat, String customerName) throws java.sql.SQLException;
    boolean isSeatFree(int sessionId, int seat) throws java.sql.SQLException;
    boolean cancelBooking(int sessionId, int seatNumber, String customerName) throws java.sql.SQLException;
}