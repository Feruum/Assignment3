package entity;

public class Session {
    public int id;
    public int movieId;
    public double price;

    public Session(int id, int movieId, double price) {
        this.id = id;
        this.movieId = movieId;
        this.price = price;
    }
}
