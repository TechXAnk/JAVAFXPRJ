module org.example.demooo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.demooo to javafx.fxml;
    exports org.example.demooo;
}