package interfaces;

public interface BookingRepositoryInterface {
    void book(int sessionId, int seat, String customerName);
    boolean isSeatFree(int sessionId, int seat);
    boolean cancelBooking(int sessionId, int seatNumber, String customerName);
}