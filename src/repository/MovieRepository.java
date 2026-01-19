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
        try {
            Connection c = DatabaseConnection.connect();
            if (c != null) {
                c.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addMovie(String title, int duration, String genre) {
        // Check input data
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Movie title cannot be empty");
            return;
        }
        if (duration <= 0) {
            System.out.println("Movie duration must be positive");
            return;
        }
        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Movie genre cannot be empty");
            return;
        }

        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot add movie: database connection failed");
                return;
            }

            String sql = "INSERT INTO movies(title, duration, genre) VALUES (?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, title);
            ps.setInt(2, duration);
            ps.setString(3, genre);
            ps.executeUpdate();

            System.out.println("Movie added successfully: " + title);
        } catch (Exception e) {
            System.out.println("Error adding movie: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
    }

    @Override
    public Movie getMovieById(int movieId) {
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get movie: database connection failed");
                return null;
            }

            String sql = "SELECT * FROM movies WHERE id=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, movieId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("duration"),
                    rs.getString("genre")
                );
                return movie;
            }
        } catch (Exception e) {
            System.out.println("Error getting movie: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return null;
    }

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Connection c = null;
        try {
            c = DatabaseConnection.connect();
            if (c == null) {
                System.out.println("Cannot get movies: database connection failed");
                return movies;
            }

            String sql = "SELECT * FROM movies ORDER BY title";
            PreparedStatement ps = c.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("duration"),
                    rs.getString("genre")
                );
                movies.add(movie);
            }
        } catch (Exception e) {
            System.out.println("Error getting movies: " + e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // Ignore close errors
                }
            }
        }
        return movies;
    }
}