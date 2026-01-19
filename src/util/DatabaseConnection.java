package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found. Make sure postgresql.jar is in classpath.");
        }
    }

    public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/Cinema",
                    "postgres",
                    "M_asdf_321"
            );
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
            return null;
        }
    }
}
