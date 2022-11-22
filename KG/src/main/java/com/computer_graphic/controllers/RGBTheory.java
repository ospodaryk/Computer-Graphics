package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class RGBTheory {

    @FXML
    private void switchToColors() throws IOException {
        App.setRoot("colors");
    }

    @FXML
    private void switchToHSL() throws IOException {
        App.setRoot("hslTheory");
    }
