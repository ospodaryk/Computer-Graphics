package com.computer_graphic.controllers;

import com.computer_graphic.App;
/*
це мова розмітки користувальницького інтерфейсу на основі XML,
створена корпорацією Oracle для визначення користувальницького інтерфейсу програми JavaFX
 */
import javafx.fxml.FXML;
import java.io.IOException;

public class MainController {

    /*@FXML
    private void switchToFractal() throws IOException {
        App.setRoot("fractal");
    }*/

    @FXML
    private void switchToColors() throws IOException {
        App.setRoot("colors");
    }
    @FXML
    private void switchToAF() throws IOException {
        App.setRoot("af");
    }
    /*@FXML
    private void switchToSquare() throws IOException {
        App.setRoot("app");
    }*/
}
