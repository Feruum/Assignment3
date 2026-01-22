package interfaces;

import entity.Session;
import java.util.List;

public interface SessionRepositoryInterface extends BaseRepository {

    void addSession(int movieId, double price, String startTime)
            throws java.sql.SQLException;

    List<Session> getAvailableSessions() throws java.sql.SQLException;

    Session getSessionById(int id) throws java.sql.SQLException;

    List<Session> getSessionsByMovieId(int movieId) throws java.sql.SQLException;
}
