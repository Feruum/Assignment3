package interfaces;

import entity.Movie;
import java.util.List;

public interface MovieRepositoryInterface extends BaseRepository {
    void addMovie(String title, int duration, String genre) throws java.sql.SQLException;
    Movie getMovieById(int movieId) throws java.sql.SQLException;
    List<Movie> getAllMovies() throws java.sql.SQLException;
}