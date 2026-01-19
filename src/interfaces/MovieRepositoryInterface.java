package interfaces;

import entity.Movie;

/**
 * Interface for movie work
 */
public interface MovieRepositoryInterface extends BaseRepository {

    /**
     * Add new movie to database
     * @param title movie name
     * @param duration time in minutes
     * @param genre movie type
     */
    void addMovie(String title, int duration, String genre);

    /**
     * Get movie by ID
     * @param movieId movie ID
     * @return Movie object or null if not found
     */
    Movie getMovieById(int movieId);

    /**
     * Get all movies
     * @return list of all movies
     */
    java.util.List<Movie> getAllMovies();
}