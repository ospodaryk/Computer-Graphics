package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class MarkingsController {
    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("main");
    }
}
