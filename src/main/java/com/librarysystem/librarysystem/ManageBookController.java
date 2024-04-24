package com.librarysystem.librarysystem;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class ManageBookController extends Main{

    @FXML
    private TextField searchTextField;
    @FXML
    private TextField currentPage;
    @FXML
    private Button forwardButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField pagesCount;
    @FXML
    private VBox List;
    @FXML
    private Button addBookButton;
    @FXML
    private Button goBackButton;
    private int pages;
    private int current;
    private AtomicReference<ResultSet> resultSet;
    private boolean isMovingForward = true;

    Pane create(String name_, String author_) {
        Pane pane = new Pane();
        pane.setPrefSize(750, 45);
        pane.setPadding(new Insets(10,10,10,10));
        pane.setStyle("-fx-background-color: #d9d9d9;");
        Label name = new Label(name_);
        name.setLayoutX(14.0);
        name.setLayoutY(10.0);
        name.setPrefSize(300,25);
        name.setAlignment(Pos.CENTER_LEFT);
        Label author = new Label(author_);
        author.setAlignment(Pos.CENTER);
        author.setLayoutX(314.0);
        author.setLayoutY(10.0);
        author.setPrefSize(300,25);
        Button button = new Button("Delete");
        button.setLayoutX(655.0);
        button.setLayoutY(10.0);
        button.setPrefSize(70,25);
        pane.getChildren().addAll(name, author, button);

        button.setOnAction(event -> {
            System.out.println("Button clicked for element " + name);
            DatabaseHandler db = new DatabaseHandler();
            String nameStr = name.getText();
            String authorStr = author.getText();
            try {
                db.deleteBook(authorStr, nameStr);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return pane;
    }

    @FXML
    void initialize() throws SQLException {
        List.setSpacing(5);
        DatabaseHandler db = new DatabaseHandler();
        current = 1;
        resultSet = new AtomicReference<>(db.getLastBooks());
        int records = db.getDbLength();
        pages = (int)Math.ceil(records / 10.0);
        pagesCount.setText(Integer.toString(pages));

        for (int i = 0; i < 10; i++) {
            if (resultSet.get().next())
                List.getChildren().add(create(
                        resultSet.get().getString("name"),
                        resultSet.get().getString("author")
                ));
            else break;
        }

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                try {
                    List.getChildren().clear();
                    resultSet.set(db.getLastBooks());
                    current = 1;
                    currentPage.setText(Integer.toString(current));
                    pages = (int)Math.ceil(db.getDbLength() / 10.0);
                    pagesCount.setText(Integer.toString(pages));
                    isMovingForward = true;
                    for (int i = 0; i < 10; i++) {
                        if (resultSet.get().next())
                            List.getChildren().add(create(
                                    resultSet.get().getString("name"),
                                    resultSet.get().getString("author")
                            ));
                        else break;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            List.getChildren().clear();
            current = 1;
            currentPage.setText(Integer.toString(current));
            isMovingForward = true;
            try {
                resultSet.set(db.getBooksByAuthor(newValue));
                pages = (int)Math.ceil(db.getDbLength() / 10.0);
                pagesCount.setText(Integer.toString(pages));

                for (int i = 0; i < 10; i++) {
                    if (resultSet.get().next())
                        List.getChildren().add(create(
                                resultSet.get().getString("name"),
                                resultSet.get().getString("author")
                        ));
                    else break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        forwardButton.setOnAction(event -> {
            if (current == pages) return;
            List.getChildren().clear();

            try {
                if (!isMovingForward){
                    for (int i = 0; i < 9; i++) {
                        resultSet.get().next();
                    }
                    isMovingForward = true;
                }

                for (int i = 0; i < 10; i++) {
                    if (resultSet.get().next()) {
                        List.getChildren().add(create(
                                resultSet.get().getString("name"),
                                resultSet.get().getString("author")
                        ));
                    }
                    else break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            currentPage.setText(Integer.toString(++current));
        });

        backButton.setOnAction(event -> {
            if (current == 1) return;

            try {
                if (resultSet.get().getRow() == 0) resultSet.get().previous();

                if (isMovingForward){
                    for (int i = 0; i < List.getChildren().size() - 1; i++) {
                        resultSet.get().previous();
                    }
                    isMovingForward = false;
                }
                List.getChildren().clear();

                for (int i = 0; i < 10; i++) {
                    if (resultSet.get().previous()) {
                        List.getChildren().add(0, create(
                                resultSet.get().getString("name"),
                                resultSet.get().getString("author")
                        ));
                    }
                    else break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            currentPage.setText(Integer.toString(--current));
        });

        addBookButton.setOnAction(event -> {
            try {
                switchToScene(event, "AddBook.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        goBackButton.setOnAction(event -> {
            try {
                switchToScene(event, "MainPage.fxml");
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        db.getBooksByAuthor("к");
    }
}
