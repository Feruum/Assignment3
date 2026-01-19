package entity;

public class Hall {
    private int id;
    private String name;
    private int totalSeats;

    public Hall(String name, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
    }

    public Hall(int id, String name, int totalSeats) {
        this.id = id;
        this.name = name;
        this.totalSeats = totalSeats;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
}
