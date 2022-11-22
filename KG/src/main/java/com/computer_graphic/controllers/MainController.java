package com.computer_graphic.controllers;

import com.computer_graphic.App;
/*
це мова розмітки користувальницького інтерфейсу на основі XML,
створена корпорацією Oracle для визначення користувальницького інтерфейсу програми JavaFX
 */
import javafx.fxml.FXML;
import java.io.IOException;

public class MainController {

    @FXML
    private void exit() throws IOException {
        App.setRoot("first_page");
    }
    @FXML
    private void switchToFractal() throws IOException {
        App.setRoot("fractal");
    }

    /*
    try{
            str = TurnComboBox.getValue().toString();
        }catch(NullPointerException e){
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setHeaderText("Оберіть точку обертання!");
            errorAlert.setContentText("Оберіть точку, навколо якої буде обертатися шестикутник");
            errorAlert.showAndWait();
        }
        int start = str.indexOf("(");
        int end = str.indexOf(")");
        String outStr = str.substring(start + 2, end - 1);
        System.out.println(outStr);
        String[] numbers = outStr.split(" , ");
     */

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
