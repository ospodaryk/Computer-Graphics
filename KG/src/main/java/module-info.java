module Application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.computer_graphic to javafx.fxml;
    exports com.computer_graphic;
    exports com.computer_graphic.controllers;
    opens com.computer_graphic.controllers to javafx.fxml;
}