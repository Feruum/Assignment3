package interfaces;

import entity.Session;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for session work
 */
public interface SessionRepositoryInterface extends BaseRepository {

    /**
     * Add new session
     * @param movieId movie ID
     * @param price ticket price
     * @param startTime session start time
     * @param hallId hall ID
     */
    void addSession(int movieId, double price, LocalDateTime startTime, int hallId);

    /**
     * Get all available sessions (not started yet)
     * @return list of available sessions
     */
    List<Session> getAvailableSessions();

    /**
     * Get session by ID
     * @param id session ID
     * @return Session object or null if not found
     */
    Session getSessionById(int id);

    /**
     * Get all sessions for one movie
     * @param movieId movie ID
     * @return list of sessions for movie
     */
    List<Session> getSessionsByMovieId(int movieId);
}