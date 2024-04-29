package com.librarysystem.librarysystem;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class AddBookController extends Main{
    @FXML
    private CheckComboBox<Genre> genreCheckBox;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField numberField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField nameField;
    @FXML
    private Button goBack;

    DatabaseHandler db = new DatabaseHandler();

    @FXML
    void initialize() throws IOException, SQLException {
        for(Genre elem : db.getGenres()){
            genreCheckBox.getItems().add(elem);
            System.out.println(elem);
        }

        confirmButton.setOnAction(event -> {
            String author = authorField.getText();
            String name = nameField.getText();
            int number = Integer.parseInt(numberField.getText());
            ObservableList<Genre> genres = genreCheckBox.getCheckModel().getCheckedItems();
            if (author.length() < 2 || name.length() < 2 || genres.isEmpty()) return;
            try {
                int idbook = db.createBook(author, name, number);
                if (idbook == 0){
                    //вывести окно об неправильных данных
                    return;
                }
                for (Genre elem : genres){
                    db.addBooksToGenre(idbook, elem.id);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        goBack.setOnAction(event -> {
            try {
                switchToScene(event, "ManageBook.fxml");
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }
}
