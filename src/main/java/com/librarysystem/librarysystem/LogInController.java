package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    public void showErrorEmptyAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Oops, I think you got it wrong.");
        alert.setContentText("Fields must not be empty");

        alert.showAndWait();
    }
    @FXML
    void initialize() throws IOException {
        logInButton.setOnAction(event ->{
            String email =emailLogInField.getText();
            String password = passwordLogInField.getText();

            if (email.isEmpty() || password.isEmpty()){
                showErrorEmptyAlert();
            }
            DatabaseHandler db = new DatabaseHandler();
            try{
                User user = db.getUserByEmail(email, password);
                if (user == null)
                    "IDITE NAHUI";
                "VHOD V AKAUNT"
            }catch (Exception e){
                System.out.println(e);
            }
        });
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
