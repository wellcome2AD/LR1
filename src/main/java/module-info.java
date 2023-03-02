module com.example.lr1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.lr1 to javafx.fxml;
    exports com.example.lr1;
}