module com.example.lr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.google.gson;

    opens com.app to javafx.fxml;
    exports com.app;
    exports com.client;
    opens com.client to com.google.gson, javafx.fxml;
    opens com.server to com.google.gson;
}