package com.librarysystem.librarysystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController extends Main{
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button addBookButton;


    @FXML
    private Button logInRegistrButton;


    @FXML
    void initialize() throws IOException {
        addBookButton.setOnAction(event -> {
            try {
                switchToScene(event, "AddBook.fxml");
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        logInRegistrButton.setOnAction(event -> {
            try {
                switchToScene(event, "LogIn.fxml");
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }
}