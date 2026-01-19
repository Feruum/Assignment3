package entity;

import java.time.LocalDateTime;

public class Session {
    public int id;
    public int movieId;
    public double price;
    public LocalDateTime startTime;
    public int hallId;

    public Session(int id, int movieId, double price, LocalDateTime startTime, int hallId) {
        this.id = id;
        this.movieId = movieId;
        this.price = price;
        this.startTime = startTime;
        this.hallId = hallId;
    }

    // Constructor for new sessions
    public Session(int movieId, double price, LocalDateTime startTime, int hallId) {
        this.movieId = movieId;
        this.price = price;
        this.startTime = startTime;
        this.hallId = hallId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getHallId() {
        return hallId;
    }
}
