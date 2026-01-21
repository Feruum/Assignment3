package repository;

import entity.Movie;
import interfaces.MovieRepositoryInterface;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository implements MovieRepositoryInterface {

    @Override
    public boolean testConnection() {
        try (Connection c = DatabaseConnection.connect()) {
            return c != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addMovie(String title, int duration, String genre) {

        // Simple validation
        if (title == null || title.isEmpty()) {
            System.out.println("Title is empty");
            return;
        }
        if (duration <= 0) {
            System.out.println("Duration must be positive");
            return;
        }
        if (genre == null || genre.isEmpty()) {
            System.out.println("Genre is empty");
            return;
        }

        String sql = "INSERT INTO movies(title, duration, genre) VALUES (?, ?, ?)";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setInt(2, duration);
            ps.setString(3, genre);
            ps.executeUpdate();

            System.out.println("Movie added");

        } catch (Exception e) {
            System.out.println("Error adding movie: " + e.getMessage());
        }
    }

    @Override
    public Movie getMovieById(int movieId) {
        String sql = "SELECT * FROM movies WHERE id=?";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("genre")
                );
            }

        } catch (Exception e) {
            System.out.println("Error getting movie: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY title";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("genre")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error getting movies: " + e.getMessage());
        }
        return movies;
    }
}
