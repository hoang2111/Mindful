module com.example.meditationapp2_0 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires com.google.gson;

    opens com.example.meditationapp2_0 to javafx.fxml, com.google.gson;
    exports com.example.meditationapp2_0;
}