package entity;

public class Movie {
    private int id;
    private String title;
    private int duration;
    private String genre;

    public Movie(String title, int duration, String genre) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
}
