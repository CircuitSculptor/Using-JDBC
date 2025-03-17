package ie.atu.week6;

import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class InsertExample {

    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();

        // Load from the classpath. Note the leading "/".
        try (InputStream input = SelectExampleWithProperties.class
                .getResourceAsStream("/db.properties")) {   // Not commiting db.properties as I created my MySQL account using a password derived from my regular

            // If the file isn't found, getResourceAsStream returns null
            if (input == null) {
                System.err.println("Unable to find db.properties on the classpath.");
                return;
            }
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return; // Exit if properties can't be loaded
        }

        // Read the properties
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, username, password)){

            // Insert a new record into the "users" table using a prepared statement.
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            stmt.setString(1, "Mark");
            stmt.setString(2, "technology");
            stmt.executeUpdate();

            // Insert a new record into the "emails" table, referencing the new user
            stmt = conn.prepareStatement("INSERT INTO emails (user_id, email) VALUES (?, ?)");
            stmt.setInt(1, getLastInsertId(conn));
            stmt.setString(2, "mark1@atu.ie");
            stmt.executeUpdate();

            System.out.println("Insert completed successfully.");
        } catch (SQLException ex) {

            System.out.println("Record insert failed.");
            ex.printStackTrace();
        }
        // Close the connection
        //conn.close();
    }

    // Helper method to get the ID of the last inserted record
    private static int getLastInsertId(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        rs.next();
        int id = rs.getInt(1);
        rs.close();
        stmt.close();
        return id;
    }
}
