package repository;

import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class MovieRepository {

    public void addMovie(String title) {
        try {
            Connection c = DatabaseConnection.connect();
            Statement s = c.createStatement();

            String sql = "INSERT INTO movies(title) VALUES ('" + title + "')";
            s.executeUpdate(sql);

            System.out.println("Movie added");
            c.close();
        } catch (Exception e) {
            System.out.println("Error adding movie");
        }
    }
}