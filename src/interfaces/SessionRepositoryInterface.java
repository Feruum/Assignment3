package interfaces;

import entity.Session;
import java.util.List;

public interface SessionRepositoryInterface extends BaseRepository {
    void addSession(int movieId, double price, String startTime, int hallId);
    List<Session> getAvailableSessions();
    Session getSessionById(int id);
    List<Session> getSessionsByMovieId(int movieId);
}