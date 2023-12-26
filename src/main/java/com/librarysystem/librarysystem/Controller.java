package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
            /*String name = nameField.getText();
            String genre = genreField.getText();
            int number = Integer.parseInt(numberField.getText());
            if (author.length() < 2 || name.length() < 2) return;*/
            DatabaseHandler db = new DatabaseHandler();
            try {
                //db.createBook(author, name, genre, number);
                //System.out.println(db.getBookById(1));
                //System.out.println(db.getBookById(10));
                Book[] books = db.getBooksByAuthor(author);
                if (books == null) {
                    System.out.println("EMPTY");
                    return;
                }
                for (Book book : books){
                    System.out.println(book);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }
}
