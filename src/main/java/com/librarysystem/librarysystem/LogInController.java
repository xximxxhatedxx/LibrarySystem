package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LogInController extends Main{
    @FXML
    private Button goToRegistrationButton;
    @FXML
    private Button logInButton;
    @FXML
    private TextField emailLogInField;
    @FXML
    private TextField passwordLogInField;
    @FXML
    private Button goBack;

    @FXML
    void initialize() throws IOException {
        goToRegistrationButton.setOnAction(event -> {
            try {
                switchToScene(event, "Registration.fxml");
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
    }
}
