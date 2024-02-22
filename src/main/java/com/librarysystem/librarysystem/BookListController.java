package com.librarysystem.librarysystem;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

class ListElement {
    static Pane create(String name_, String author_) {
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
        //layoutX="655.0" layoutY="13.0" mnemonicParsing="false" prefHeight="25.0" prefWidth="70.0" text="TAKE"
        Button button = new Button("TAKE");
        button.setLayoutX(655.0);
        button.setLayoutY(10.0);
        button.setPrefSize(70,25);
        pane.getChildren().addAll(name, author, button);
        return pane;
    }
}

public class BookListController extends Main{

    @FXML
    private TextField currentPage;
    @FXML
    private Button forward;
    @FXML
    private Button back;
    @FXML
    private TextField pagesCount;
    @FXML
    private VBox List;

    private int pages;
    private int current;

    @FXML
    void initialize() throws SQLException {
        List.setSpacing(5);
        DatabaseHandler db = new DatabaseHandler();
        int records = db.getDbLength();
        pages = (int)Math.ceil(records / 10.0);
        current = 0;
        pagesCount.setText(Integer.toString(pages));
        final Book[][] books = {db.getLastBooks(0)};
        for (Book book : books[0])
            List.getChildren().add(ListElement.create(book.name, book.author));

        forward.setOnAction(event -> {
            if (current + 1 == pages) return;
            List.getChildren().clear();
            try {
                books[0] = db.getLastBooks(++current);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (Book book : books[0])
                List.getChildren().add(ListElement.create(book.name, book.author));
            currentPage.setText(Integer.toString(current+1));
        });

        back.setOnAction(event -> {
            if (current == 0) return;
            List.getChildren().clear();
            try {
                books[0] = db.getLastBooks(--current);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (Book book : books[0])
                List.getChildren().add(ListElement.create(book.name, book.author));
            currentPage.setText(Integer.toString(current+1));
        });
    }
}
