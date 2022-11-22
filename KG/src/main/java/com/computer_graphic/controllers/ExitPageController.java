package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class ExitPageController {

    @FXML
    private void switchToFractal() throws IOException {
        App.setRoot("exit");
    }
}
