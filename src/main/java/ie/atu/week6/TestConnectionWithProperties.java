package ie.atu.week6;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnectionWithProperties {
    public static void main(String[] args) {
        Properties props = new Properties();

        // Load from the classpath. Note the leading "/".
        try (InputStream input = TestConnectionWithProperties.class
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

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection made to mydatabasedrapinski2");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
