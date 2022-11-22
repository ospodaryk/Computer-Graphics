package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class AFTheoryController {

    @FXML
    private void switchToAF() throws IOException {
        App.setRoot("af");
    }
}
