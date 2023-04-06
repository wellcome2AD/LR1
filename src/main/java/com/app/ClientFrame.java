package com.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientFrame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientFrame.class.getResource("client-frame.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Меткий стрелок");
        stage.setScene(scene);
        stage.show();
    }

    public static void StartApp() {
        launch();
    }
    /*public static void main(String args[]) {
        launch();
    }*/
}