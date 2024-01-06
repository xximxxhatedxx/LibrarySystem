package com.librarysystem.librarysystem;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.sql.SQLException;

public class Controller {
    public CheckComboBox genreCheckBox;
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
        final int[] page = {0};
        confirmButton.setOnAction(event -> {
            /*String author = authorField.getText();
            String name = nameField.getText();
            ObservableList<String> genre = genreCheckBox.getCheckModel().getCheckedItems();
            int number = Integer.parseInt(numberField.getText());
            if (author.length() < 2 || name.length() < 2 || genre.isEmpty()) return;*/
            DatabaseHandler db = new DatabaseHandler();
            try {
                //db.createBook(author, name, String.join(",", genre), number);
                //System.out.println(db.getBookById(1));
                //System.out.println(db.getBookById(10));
                Book[] books = db.getLastBooks(page[0]++ * 20);
                for (Book book : books){
                    System.out.println(book);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println();
        });
    }
}
