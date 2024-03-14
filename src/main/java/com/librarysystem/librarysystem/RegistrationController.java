package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

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
    private void showErrorAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Oops, I think you got it wrong.");
        alert.setContentText("Passwords do not match");

        alert.showAndWait();
    }
    public void showErrorEmptyAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Oops, I think you got it wrong.");
        alert.setContentText("Fields must not be empty");

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
                showErrorAlert();
            }else if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty())
                { showErrorEmptyAlert();}
            DatabaseHandler db = new DatabaseHandler();
            try{
                db.addUser(name, surname, email, password);
                switchToScene(event, "Main.fxml");
            }catch (Exception e){
                System.out.println(e);
            }
            System.out.println();
        });

    }
}
