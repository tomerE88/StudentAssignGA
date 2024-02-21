module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.View to javafx.fxml;
    exports com.example.View;
}
