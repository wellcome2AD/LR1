package com.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerFrame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerFrame.class.getResource("server-frame.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Меткий стрелок");
        stage.setScene(scene);
    }

    public static void StartApp() {
        launch();
    }
}