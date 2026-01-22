package entity;

public class Session {
    public int id;
    public int movieId;
    public double price;
    public String startTime; // Просто строка для времени

    public Session(int id, int movieId, double price, String startTime) {
        this.id = id;
        this.movieId = movieId;
        this.price = price;
        this.startTime = startTime;
    }

    // Constructor for new sessions
    public Session(int movieId, double price, String startTime) {
        this.movieId = movieId;
        this.price = price;
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }
    public double getPrice() {
        return price;
    }
}
