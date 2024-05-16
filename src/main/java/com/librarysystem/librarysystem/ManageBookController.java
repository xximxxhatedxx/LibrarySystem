package com.librarysystem.librarysystem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckListView;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class ManageBookController extends Main{
    @FXML
    private ToggleButton authorButton;
    @FXML
    private ToggleButton nameButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField currentPage;
    @FXML
    private Button searchButton;
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
    @FXML
    private ToggleGroup Search;
    @FXML
    private CheckListView GenreList;
    private int pages;
    private int current;
    private AtomicReference<ResultSet> resultSet;
    private boolean isMovingForward = true;
    DatabaseHandler db = new DatabaseHandler();


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
        Button button = new Button();
        button.setLayoutX(655.0);
        button.setLayoutY(10.0);
        button.setPrefSize(70,25);
        pane.getChildren().addAll(name, author, button);
        String nameStr = name.getText();
        String authorStr = author.getText();
        boolean status;
        try{
            status = db.statusBook(authorStr, nameStr);
            if(status){
                button.setText("Active");
            }else{
                button.setText("Inactive");
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        button.setOnAction(event -> {
            System.out.println("Button clicked for element " + name);
            try {
                if(status){
                    button.setText("Inactive");
                    db.inactiveBook(authorStr, nameStr);
                }else{
                    button.setText("Active");
                    db.activateBook(authorStr, nameStr);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return pane;
    }
    void changeList(){
        List.getChildren().clear();
        current = 1;
        currentPage.setText(Integer.toString(current));
        isMovingForward = true;
        try {
            String searchText = searchTextField.getText();
            ObservableList<Genre> genres = GenreList.getCheckModel().getCheckedItems();
            if(searchText == null || searchText.isEmpty() || searchText.isBlank())
                resultSet.set(db.getLastBooks(genres));
            else if (Search.getSelectedToggle() == nameButton)
                resultSet.set(db.getBooksByName(searchText, genres));
            else if (Search.getSelectedToggle() == authorButton)
                resultSet.set(db.getBooksByAuthor(searchText, genres));

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
    }

    @FXML
    void initialize() throws SQLException {
            List.setSpacing(5);
            resultSet = new AtomicReference<ResultSet>();

            changeList();

            searchButton.setOnAction(event -> {
                changeList();
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

            nameButton.setOnAction(event -> nameButton.setDisable(true));
            authorButton.setOnAction(event -> authorButton.setDisable(true));

            Search.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldValue, Toggle newValue) {
                    if (newValue == nameButton){
                        authorButton.setDisable(false);
                    }
                    else{
                        nameButton.setDisable(false);
                    }
                }
            });

            try{
                DatabaseHandler db = new DatabaseHandler();
                Genre[] genres = db.getGenres();
                for(Genre genre: genres){
                    GenreList.getItems().add(genre);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
            addBookButton.setOnAction(event->{
                try {
                    switchToScene(event, "AddBook.fxml");
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
            goBackButton.setOnAction(event->{
                try {
                    switchToScene(event, "LogIn.fxml");
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
    }
}
