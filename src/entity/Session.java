package entity;

import java.time.LocalDateTime;

public class Session {
    private int id;
    private int movieId;
    private int hallId;
    private LocalDateTime time;
    private double price;

    public Session(int movieId, int hallId, LocalDateTime time, double price) {
        this.movieId = movieId;
        this.hallId = hallId;
        this.time = time;
        this.price = price;
    }

    public int getId() { return id; }
}
