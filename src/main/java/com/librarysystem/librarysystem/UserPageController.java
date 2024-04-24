package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;

public class UserPageController extends Main {
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Button backToBookListButton;
    @FXML
    private Button logOutButton;
    private ModuleLayer.Controller LogInController;

    private void setUserInfo(User user) {
        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        emailLabel.setText(user.getEmail());
    }
    @FXML
    void initialize() throws IOException {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        setUserInfo(currentUser);
        logOutButton.setOnAction(event ->{
            try {
                CurrentSession.getInstance().deleteSession();
                switchToScene(event, "MainPage.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
