package com.librarysystem.librarysystem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BookListController extends Main{

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
    private ToggleGroup Search;
    @FXML
    private CheckListView GenreList;
    @FXML
    private Button logOut;
    @FXML
    private Button myPage;

    private int pages;
    private int current;
    private AtomicReference<ResultSet> resultSet;
    private boolean isMovingForward = true;
    DatabaseHandler db = new DatabaseHandler();
    CurrentSession session = CurrentSession.getInstance();

    Pane create(Integer id_, String name_, String author_, String genres_, Integer number_, Boolean status_) throws SQLException {
        Pane pane = new Pane();
        pane.setPrefSize(750, 45);
        pane.setPadding(new Insets(10,10,10,10));
        pane.getStyleClass().add("backBookFon");
        Label name = new Label(name_);
        name.getStyleClass().add("label");
        name.setLayoutX(14.0);
        name.setLayoutY(10.0);
        name.setPrefSize(300,25);
        name.setAlignment(Pos.CENTER_LEFT);
        Label author = new Label(author_);
        author.getStyleClass().add("label");
        author.setAlignment(Pos.CENTER);
        author.setLayoutX(314.0);
        author.setLayoutY(10.0);
        author.setPrefSize(300,25);
        Button button = new Button("TAKE");
        button.getStyleClass().add("take");
        button.setOnAction(event -> {
            try {
                db.manageBook(id_ ,session.getCurrentUser().id, true);
                button.setDisable(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        if (number_ == 0 || status_) button.setDisable(true);
        button.setLayoutX(655.0);
        button.setLayoutY(10.0);
        button.setPrefSize(70,25);
        pane.getChildren().addAll(name, author, button);
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
            User currentUser = session.getCurrentUser();
            if(searchText == null || searchText.isEmpty() || searchText.isBlank())
                resultSet.set(db.getLastBooks(genres, currentUser.id, currentUser.isAdmin));
            else if (Search.getSelectedToggle() == nameButton)
                resultSet.set(db.getBooksByName(searchText, genres, currentUser.id, currentUser.isAdmin));
            else if (Search.getSelectedToggle() == authorButton)
                resultSet.set(db.getBooksByAuthor(searchText, genres, currentUser.id, currentUser.isAdmin));

            pages = (int)Math.ceil(db.getDbLength() / 10.0);
            pagesCount.setText(Integer.toString(pages));

            for (int i = 0; i < 10; i++) {
                if (resultSet.get().next())
                    List.getChildren().add(create(
                            resultSet.get().getInt("idbooks"),
                            resultSet.get().getString("name"),
                            resultSet.get().getString("author"),
                            resultSet.get().getString("genres"),
                            resultSet.get().getInt("number"),
                            resultSet.get().getBoolean("status")
                    ));
                else break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize(){
        List.setSpacing(5);
        resultSet = new AtomicReference<ResultSet>();

        changeList();

        searchButton.setOnAction(event -> {
            changeList();
        });
        logOut.setOnAction(event->{
            try{
                switchToScene(event, "LogIn.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        myPage.setOnAction(event->{
            try{
                switchToScene(event, "UserPage.fxml");
            } catch (IOException e) {
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
                                resultSet.get().getInt("idbooks"),
                                resultSet.get().getString("name"),
                                resultSet.get().getString("author"),
                                resultSet.get().getString("genres"),
                                resultSet.get().getInt("number"),
                                resultSet.get().getBoolean("status")
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
                                resultSet.get().getInt("idbooks"),
                                resultSet.get().getString("name"),
                                resultSet.get().getString("author"),
                                resultSet.get().getString("genres"),
                                resultSet.get().getInt("number"),
                                resultSet.get().getBoolean("status")
                        ));
                    }
                    else break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            currentPage.setText(Integer.toString(--current));
        });
        nameButton.setDisable(true);
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

    }
}