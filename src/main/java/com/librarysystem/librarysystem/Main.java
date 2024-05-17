package com.librarysystem.librarysystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Parent root;
    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage _stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        _stage.setTitle("Library");
        _stage.setScene(new Scene(root));
        _stage.setResizable(false);
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        _stage.getIcons().add(icon);
        _stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void switchToScene(ActionEvent event, String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
