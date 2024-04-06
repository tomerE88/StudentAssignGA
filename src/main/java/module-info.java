module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens com.example.Controller to javafx.fxml;
    exports com.example.View;
}
