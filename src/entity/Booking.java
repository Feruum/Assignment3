package entity;

public class Booking {
    public int sessionId;
    public int seatNumber;
    public String customer;

    public Booking(int sessionId, int seatNumber, String customer) {
        this.sessionId = sessionId;
        this.seatNumber = seatNumber;
        this.customer = customer;
    }
}
