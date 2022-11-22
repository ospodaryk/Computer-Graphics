package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FirstPageController {

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("markings");
    }

    @FXML
    private void closeProgram() throws IOException {
        App.setRoot("exit");
    }

}
