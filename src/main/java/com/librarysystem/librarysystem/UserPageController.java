package com.librarysystem.librarysystem;

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

public class UserPageController extends Main {
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Button backToBookListButton;
    @FXML
    private Button logOutButton;
    private ModuleLayer.Controller LogInController;

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

    private int pages;
    private int current;
    private AtomicReference<ResultSet> resultSet;
    private boolean isMovingForward = true;

    private void setUserInfo(User user) {
        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        emailLabel.setText(user.getEmail());
    }

    Pane create(String name_, String author_) {
        Pane pane = new Pane();
        pane.setPrefSize(450, 45);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setStyle("-fx-background-color: #d9d9d9;");
        Label name = new Label(name_);
        name.setLayoutX(14.0);
        name.setLayoutY(10.0);
        name.setPrefSize(300, 25);
        name.setAlignment(Pos.CENTER_LEFT);
        Label author = new Label(author_);
        author.setAlignment(Pos.CENTER);
        author.setLayoutX(150.0);
        author.setLayoutY(10.0);
        author.setPrefSize(300, 25);
        Button button = new Button("TAKE");
        button.setLayoutX(450.0);
        button.setLayoutY(10.0);
        button.setPrefSize(70, 25);
        pane.getChildren().addAll(name, author, button);
        return pane;
    }

    @FXML
    void initialize() throws IOException, SQLException {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        setUserInfo(currentUser);

        logOutButton.setOnAction(event -> {
            try {
                CurrentSession.getInstance().deleteSession();
                switchToScene(event, "MainPage.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List.setSpacing(5);
        DatabaseHandler db = new DatabaseHandler();
        current = 1;
        resultSet = new AtomicReference<>(db.getBokByUser(currentUser));
        int records = db.getDbLength();
        pages = (int) Math.ceil(records / 10.0);
        pagesCount.setText(Integer.toString(pages));

        for (int i = 0; i < 10; i++) {
            if (resultSet.get().next())
                List.getChildren().add(create(
                        resultSet.get().getString("name"),
                        resultSet.get().getString("author")
                ));
            else break;
        }

        forwardButton.setOnAction(event -> {
            if (current == pages) return;
            List.getChildren().clear();

            try {
                if (!isMovingForward) {
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
                    } else break;
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

                if (isMovingForward) {
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
                    } else break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            currentPage.setText(Integer.toString(--current));
        });
    }
}