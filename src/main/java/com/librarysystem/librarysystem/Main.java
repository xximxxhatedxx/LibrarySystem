package com.librarysystem.librarysystem;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("AddBook.fxml"));
        stage.setTitle("main-page");
        stage.setScene(new Scene(root, 1210,810));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
