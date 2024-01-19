package org.example.demooo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseHandler {
    private static final String url = "jdbc:mysql://localhost:3306/ankit";
    private static final String username = "root";
    private static final String password = "";

    public DatabaseHandler() {
        // Load the JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void storeData(String name, int age) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Prepare SQL statement
            String sql = "INSERT INTO user (name, age) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);

                // Execute the insert statement
                preparedStatement.executeUpdate();
                System.out.println("inserted successfully");

            }
        } catch (Exception e) {
            System.out.println("Error storing data in the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
//    public static void displayDataFromDatabase() {
//        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ankit");
//             PreparedStatement preparedStatement = connection.prepareStatement(
//                     "SELECT * FROM user");
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//
//            while (resultSet.next()) {
//                System.out.println("Name: " + resultSet.getString("name") +
//                        ", Age: " + resultSet.getInt("age"));
//                String a= String.valueOf(resultSet);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}