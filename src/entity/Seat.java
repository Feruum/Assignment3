package entity;

public class Seat {
    private int id;
    private int hallId;
    private String seatNumber;

    public Seat(int hallId, String seatNumber) {
        this.hallId = hallId;
        this.seatNumber = seatNumber;
    }

    public int getId() { return id; }
}
