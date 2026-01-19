package interfaces;

/**
 * Base interface for all repositories
 * Has common methods for database work
 */
public interface BaseRepository {

    /**
     * Check database connection
     * @return true if ok, false if error
     */
    boolean testConnection();
}