package entity;

public class Booking {
    private int id;
    private int sessionId;
    private int seatId;
    private String customerName;
    private String status;

    public Booking(int sessionId, int seatId, String customerName) {
        this.sessionId = sessionId;
        this.seatId = seatId;
        this.customerName = customerName;
        this.status = "BOOKED";
    }
}
