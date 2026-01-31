package service;

import entity.Movie;
import repository.MovieRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {
    
    private final MovieRepository movieRepo;

    public MovieService() {
        this.movieRepo = new MovieRepository();
    }

    public List<Movie> searchByTitle(String query) throws java.sql.SQLException {
        String lowerQuery = query.toLowerCase();
        
        return movieRepo.getAllMovies().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<Movie> filterByGenre(String genre) throws java.sql.SQLException {
        String lowerGenre = genre.toLowerCase();
        
        return movieRepo.getAllMovies().stream()
                .filter(m -> m.getGenre().toLowerCase().contains(lowerGenre))
                .collect(Collectors.toList());
    }

    public List<Movie> sortByDuration() throws java.sql.SQLException {
        return movieRepo.getAllMovies().stream()
                .sorted(Comparator.comparingInt(Movie::getDuration))
                .collect(Collectors.toList());
    }
}
