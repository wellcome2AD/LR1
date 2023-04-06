package com.app;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public class MyDialog {
    private Dialog<String> dialog;
    private Optional<String> result;
    public MyDialog(String headerText){
        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle("Ввод имени пользователя");
        dialog.setHeaderText(headerText);
        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Зарегистрироваться", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");

        grid.add(new Label("Имя пользователя:"), 0, 0);
        grid.add(username, 1, 0);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return username.getText();
            }
            return null;
        });

        result = dialog.showAndWait();
    }
    public Optional<String> getResult() { return result; }
}
