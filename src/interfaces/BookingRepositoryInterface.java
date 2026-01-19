package interfaces;

import entity.Booking;
import java.util.List;

/**
 * Interface for booking work
 */
public interface BookingRepositoryInterface extends BaseRepository {

    /**
     * Check if seat is free
     * @param sessionId session ID
     * @param seatNumber seat number
     * @return true if free, false if taken
     */
    boolean isSeatFree(int sessionId, int seatNumber);

    /**
     * Book a seat
     * @param sessionId session ID
     * @param seatNumber seat number
     * @param customerName customer name
     */
    void book(int sessionId, int seatNumber, String customerName);

    /**
     * Get all bookings for session
     * @param sessionId session ID
     * @return list of bookings
     */
    List<Booking> getBookingsBySessionId(int sessionId);

    /**
     * Get user bookings
     * @param customerName customer name
     * @return list of user bookings
     */
    List<Booking> getBookingsByCustomer(String customerName);

    /**
     * Cancel booking
     * @param sessionId session ID
     * @param seatNumber seat number
     * @param customerName customer name
     * @return true if canceled, false if error
     */
    boolean cancelBooking(int sessionId, int seatNumber, String customerName);
}