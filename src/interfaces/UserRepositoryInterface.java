package interfaces;

import entity.User;
import java.sql.SQLException;

public interface UserRepositoryInterface {
    void addUser(User user) throws SQLException;
    User getUserByUsername(String username) throws SQLException;
    boolean userExists(String username) throws SQLException;
    void updateUserBookingCount(String username, int newCount) throws SQLException;
}
