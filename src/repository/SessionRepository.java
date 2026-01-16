package repository;

import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class SessionRepository {

    public void addSession(int movieId, double price) {
        try {
            Connection c = DatabaseConnection.connect();
            Statement s = c.createStatement();

            String sql = "INSERT INTO sessions(movie_id, price) VALUES ("
                    + movieId + ", " + price + ")";
            s.executeUpdate(sql);

            System.out.println("Session added");
            c.close();
        } catch (Exception e) {
            System.out.println("Error adding session");
        }
    }
}
