package interfaces;

import entity.Movie;
import java.util.List;

public interface MovieRepositoryInterface extends BaseRepository {
    void addMovie(String title, int duration, String genre);
    Movie getMovieById(int movieId);
    List<Movie> getAllMovies();
}