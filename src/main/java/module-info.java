module com.example.lr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;

    exports com.app;
    exports com.client;
    exports com.server;
    opens com.app to com.google.gson, javafx.fxml;
    opens com.client to com.google.gson;
    opens com.server to com.google.gson, javafx.fxml;
}