package org.example.demooo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(6666);
            System.out.println("Waiting for connection");

            while (true) {
                Socket s = ss.accept();
                System.out.println("Connection successful: " + s);

                // Read data from the client
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String name = dis.readUTF();
                int age = dis.readInt();

                System.out.println("Received name: " + name);
                System.out.println("Received age: " + age);

                // Store data in the database
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.storeData(name, age);
                System.out.println("Successfully inserted into the database");

                // Retrieve data from the database
                String responseData = retrieveDataFromDatabase();

                // Send the response back to the client
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(responseData);

                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String retrieveDataFromDatabase() {
        StringBuilder response = new StringBuilder("Data retrieved from the database:\n");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ankit","root","");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                response.append("Name: ").append(name).append(", Age: ").append(age).append("\n");

            }
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
