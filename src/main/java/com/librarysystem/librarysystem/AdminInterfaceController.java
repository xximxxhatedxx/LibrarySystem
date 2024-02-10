package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AdminInterfaceController extends Main{
    @FXML
    private Button manageBooks;
    @FXML
    private Button manageUsers;
    @FXML
    private Button logOut;

    @FXML
    void initialize() throws IOException {
        logOut.setOnAction(event -> {
            try {
                switchToScene(event, "LogIn.fxml");
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }
}
