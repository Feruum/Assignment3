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


    public void addMovie(String title, int duration, String genre) throws java.sql.SQLException {
        String sql = "INSERT INTO movies(title, duration, genre) VALUES (?, ?, ?)";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

        ps.setString(1, title);
        ps.setInt(2, duration);
        ps.setString(3, genre);
        ps.executeUpdate();

        System.out.println("Movie added");
    }

    public Movie getMovieById(int movieId) throws java.sql.SQLException {
        String sql = "SELECT * FROM movies WHERE id=?";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);

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

        return null;
    }

    public List<Movie> getAllMovies() throws java.sql.SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY title";

        Connection c = DatabaseConnection.connect();
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            movies.add(new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("duration"),
                    rs.getString("genre")
            ));
        }

        return movies;
    }
}
