package org.example.demooo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientGUI extends Application {

    private TextField nameField;
    private TextField ageField;
    private TextArea dataTextArea;
    private Socket socket;
    private DataOutputStream outputStream;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client GUI");

        // Layout setup
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");

        nameField = new TextField();
        ageField = new TextField();

        Button submitButton = new Button("Submit");
        Button refreshButton = new Button("Refresh");
        Button displayButton = new Button("Display");

        dataTextArea = new TextArea();
        dataTextArea.setEditable(false);

        // Styling
        grid.setStyle("-fx-background-color: #f4f4f4;");
        nameLabel.setStyle("-fx-font-weight: bold;");
        ageLabel.setStyle("-fx-font-weight: bold;");
        submitButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        refreshButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        displayButton.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white;");

        // Event handlers
        submitButton.setOnAction(e -> sendDataToServer());
        refreshButton.setOnAction(e -> refreshData());
        displayButton.setOnAction(e -> retrieveAndDisplayData());
        // Grid layout
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(ageLabel, 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(submitButton, 0, 2);
        grid.add(refreshButton, 1, 2);
        grid.add(displayButton, 2, 2);
        grid.add(dataTextArea, 0, 3, 3, 1);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);

        // Establish the socket connection once when the application starts
        try {
            socket = new Socket("localhost", 6666);
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to connect to server: " + e.getMessage());
        }

        primaryStage.show();
    }

    private void sendDataToServer() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());

        if (name.isEmpty() || String.valueOf(age).isEmpty()) {
            showAlert("Error", "Please enter both name and age.");
            return;
        }

        try {
            // Sending data to the server
            outputStream.writeUTF(name);
            outputStream.writeInt(age);
            outputStream.flush(); // Make sure the data is sent immediately

            showAlert("Success", "Data sent to server successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to connect to server: " + e.getMessage());
        }
    }

    private void refreshData() {
        // Clear the name and age text fields
        nameField.clear();
        ageField.clear();

        // Clear the dataTextArea
        dataTextArea.clear();
    }

    private void retrieveAndDisplayData() {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            // Receiving and displaying data from the server
            String serverResponse = inputStream.readUTF();

            // Update UI on the JavaFX Application Thread
            Platform.runLater(() -> {
                dataTextArea.appendText("Received from server: " + serverResponse + "\n");
                showAlert("Success", "Data retrieved and displayed successfully!");
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to connect to server: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        // Close the socket when the application exits
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        super.stop();
    }
}
