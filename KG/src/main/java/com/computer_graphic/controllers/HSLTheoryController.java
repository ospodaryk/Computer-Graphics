package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class HSLTheoryController {

    @FXML
    private void switchToColors() throws IOException {
        App.setRoot("colors");
    }
}
