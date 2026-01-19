package interfaces;

import entity.User;

/**
 * Interface for user work
 */
public interface UserRepositoryInterface extends BaseRepository {

    /**
     * Register new user
     * @param username user name
     * @param password user password
     * @param role user role (admin/customer)
     */
    void addUser(String username, String password, String role);

    /**
     * Check user login
     * @param username user name
     * @param password user password
     * @return User object if ok, null if error
     */
    User authenticate(String username, String password);

    /**
     * Update user booking count
     * @param userId user ID
     * @param newCount new booking count
     */
    void updateBookingCount(int userId, int newCount);

    /**
     * Check if username is taken
     * @param username username to check
     * @return true if taken, false if free
     */
    boolean isUsernameTaken(String username);
}