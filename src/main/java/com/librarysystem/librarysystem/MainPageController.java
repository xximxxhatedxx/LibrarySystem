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

public class MainPageController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button addBookButton;


    @FXML
    private Button logInRegistrButton;


    @FXML
    void initialize() throws IOException {
        final int[] page = {0};
        addBookButton.setOnAction(event -> {
            try {
                switchToAddBook(event);
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        logInRegistrButton.setOnAction(event -> {
            try {
                switchToLogInRegistrButton(event);
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }

    public void switchToAddBook(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AddBook.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogInRegistrButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("logIn.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}