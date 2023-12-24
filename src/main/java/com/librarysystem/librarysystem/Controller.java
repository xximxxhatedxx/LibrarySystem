package com.librarysystem.librarysystem;

import com.sun.javafx.scene.control.IntegerField;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class Controller {
    @FXML
    private Button confirmButton;
    @FXML
    private TextField numberField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField nameField;

    @FXML
    void initialize(){
        confirmButton.setOnAction(event -> {
            String author = authorField.getText();
            String name = nameField.getText();
            String genre = genreField.getText();
            int number = Integer.parseInt(numberField.getText());
            System.out.println(author + " - " + name + " - " + genre + " - " + number);
            if (author.length() < 2 || name.length() < 2) return;
            DatabaseHandler db = new DatabaseHandler();
            try {
                db.createBook(author, name, genre, number);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }
}
