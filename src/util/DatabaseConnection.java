package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/cinema",
                    "postgres",
                    "password"
            );
        } catch (Exception e) {
            System.out.println("Database connection error");
            return null;
        }
    }
}
