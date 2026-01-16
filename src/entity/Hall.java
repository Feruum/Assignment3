package entity;

public class Hall {
    private int id;
    private String name;
    private int totalSeats;

    public Hall(String name, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
    }

    public int getId() { return id; }
}
