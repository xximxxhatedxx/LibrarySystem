package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.SQLException;

public class RegistrationController extends Main{
    @FXML
    private Button addUserButton;
    @FXML
    private Button goToLogInButton;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswordField;
    @FXML
    private Button goBack;

    private void showErrorAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Oops, I think you got it wrong.");
        alert.setContentText(text);

        alert.showAndWait();
    }
    @FXML
    void initialize() throws IOException {
        goToLogInButton.setOnAction(event -> {
            try {
                switchToScene(event, "LogIn.fxml");
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        goBack.setOnAction(event -> {
            try {
                switchToScene(event, "MainPage.fxml");
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        addUserButton.setOnAction(event -> {
            String name = firstNameField.getText();
            String surname = lastNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String rePassword = rePasswordField.getText();
            if(!password.equals(rePassword)) {
                showErrorAlert("Passwords do not match");
            }else if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty())
                { showErrorAlert("Fields must not be empty");}
            DatabaseHandler db = new DatabaseHandler();
            try{
                db.addUser(name, surname, email, password);
                User user = new User(name, surname, email, password, false);
                CurrentSession.getInstance().setCurrentUser(user);
                switchToScene(event, "UserPage.fxml");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        });

    }
}
