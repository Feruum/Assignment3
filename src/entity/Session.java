package entity;

public class Session {
    public int id;
    public int movieId;
    public double price;
    public String startTime;
    public int hallId;

    public Session(int id, int movieId, double price, String startTime, int hallId) {
        this.id = id;
        this.movieId = movieId;
        this.price = price;
        this.startTime = startTime;
        this.hallId = hallId;
    }

    // Constructor for new sessions
    public Session(int movieId, double price, String startTime, int hallId) {
        this.movieId = movieId;
        this.price = price;
        this.startTime = startTime;
        this.hallId = hallId;
    }
    
    // Legacy constructors for backward compatibility (defaults hallId to 1)
    public Session(int id, int movieId, double price, String startTime) {
        this(id, movieId, price, startTime, 1);
    }
    public Session(int movieId, double price, String startTime) {
        this(movieId, price, startTime, 1);
    }

    public String getStartTime() {
        return startTime;
    }
    public double getPrice() {
        return price;
    }
    public int getHallId() { return hallId; } // Getter
}
