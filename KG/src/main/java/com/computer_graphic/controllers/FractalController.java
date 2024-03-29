package com.computer_graphic.controllers;

import com.computer_graphic.App;
import com.computer_graphic.Complex;
import com.computer_graphic.FractalBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static com.computer_graphic.Complex.arg;


public class FractalController implements Initializable {

    private static final int CANVAS_WIDTH = 480;
    private static final int CANVAS_HEIGHT = 320;

    private static final double FRACTAL_RE_MIN = -2;
    private static final double FRACTAL_RE_MAX = 1;
    private static final double FRACTAL_IM_MIN = -1;
    private static final double FRACTAL_IM_MAX = 1;

    @FXML
    public Canvas canvas;
    public ImageView imageView;
    public RadioButton k3;
    public RadioButton k4;

    @FXML
    public TextField cRe;

    @FXML
    public TextField cIm;
    public TextField iterations;

    @FXML
    public Slider stepSlider;

    @FXML
    public ColorPicker fractalColorPicker1;
    public ColorPicker fractalColorPicker2;
    public ColorPicker fractalColorPicker3;
    public ColorPicker fractalColorPicker4;
    public Text textSlider;

    @FXML
    private void goToTheory() throws IOException {
        App.setRoot("fractalTheory");
    }

    private final int W = 400;
    private final int H = 400;
    private final int X0 = 200;
    private final int Y0 = 200;
    private final double PI4 = 0.785398163397448;
    private final double PI3 = 1.047197551196598;
    private final double PI5 = 0.628318530717959;

    @FXML
    private void backToMain() throws IOException {
        App.setRoot("main");
    }

    private final FractalBean fractal =
            new FractalBean(50, FRACTAL_RE_MIN, FRACTAL_RE_MAX, FRACTAL_IM_MIN, FRACTAL_IM_MAX, 0, 0);

    public WritableImage fractionPixelSet4(Complex cnumber, int iteration, double pxl) {
        WritableImage outputImage = new WritableImage(W, H);
        PixelWriter writer = outputImage.getPixelWriter();
        Color color = Color.RED;
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                int slsl = iteration;
                double x = (i - X0) * pxl;
                double y = (j - Y0) * pxl;
                Complex z = new Complex(x, y);
                if (x != X0 && y != Y0) {
                    do {
                        z = z.multiply(0.66).add(z.pow(-3).multiply(cnumber));
                        slsl--;
                    } while (slsl > 0);
                }
                switch ((int) (arg(z) / PI4)) {
                    case 0:
                        color = fractalColorPicker1.getValue();
                        break;
                    case 1:
                    case 2:
                        color = fractalColorPicker2.getValue();
                        break;
                    case -3:
                    case -4:
                        color = fractalColorPicker3.getValue();
                        break;
                    case -1:
                    case -2:
                        color = fractalColorPicker4.getValue();
                        break;
                }
                writer.setColor(i, j, color);
            }
        }
        return outputImage;
    }

    public WritableImage fractionPixelSet3(Complex cnumber, int iteration, double pxl) {
        WritableImage outputImage = new WritableImage(W, H);
        PixelWriter writer = outputImage.getPixelWriter();
        Color color = Color.RED;
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                int slsl = iteration;
                double x = (i - X0) * pxl;
                double y = (j - Y0) * pxl;
                Complex z = new Complex(x, y);
                if (x != X0 && y != Y0) {
                    do {
                        z = z.multiply(0.66).add((z.pow(-2).multiply(cnumber)));
                        slsl--;
                    } while (slsl > 0);
                }
                switch ((int) (arg(z) / PI3)) {
                    case 0:
                        color = fractalColorPicker1.getValue();
                        break;
                    case 1:
                    case 2:
                        color = fractalColorPicker2.getValue();
                        break;
                    case -1:
                    case -2:
                        color = fractalColorPicker3.getValue();
                        break;
                }

                writer.setColor(i, j, color);
            }
        }
        return outputImage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        k3.setText("K = 3");
        k4.setText("K = 4");
        ToggleGroup toggleGroup = new ToggleGroup();
        k3.setToggleGroup(toggleGroup);
        k4.setToggleGroup(toggleGroup);
        k3.setSelected(true);
        Complex num = new Complex(1, 1);

        AtomicInteger iteration = new AtomicInteger(30);

        iterations.setText("30");

        final double[] pxl = {0.02};

        imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
        fractalColorPicker1.setOnAction(e -> {
            fractal.setColorSchema(fractalColorPicker1.getValue());
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }
        });

        iterations.setOnAction(e -> {
            Integer tmp;
            try{
                tmp = Integer.parseInt(iterations.getText());
            }catch (Exception exception){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Неправильне введена кількість ітерацій!");
                errorAlert.setContentText("Потрібнно ввести ціле число. Наприклад: 3, 6, 30\nЗначення за замовчуванням - 30");
                errorAlert.showAndWait();
                tmp = 30;
                iterations.setText("30");
            }
            iteration.set(tmp);
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }
        });

        fractalColorPicker2.setOnAction(e -> {
            fractal.setColorSchema(fractalColorPicker2.getValue());
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }
        });
        fractalColorPicker3.setOnAction(e -> {
            fractal.setColorSchema(fractalColorPicker3.getValue());
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }
        });
        fractalColorPicker4.setOnAction(e -> {
            fractal.setColorSchema(fractalColorPicker4.getValue());
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }
        });
        k3.setOnAction(e -> {
            fractalColorPicker4.setLayoutX(6120);

            num.setRe(Double.parseDouble(cRe.getText()));
            num.setIm(Double.parseDouble(cIm.getText()));
            imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
        });

        k4.setOnAction(e -> {
            fractalColorPicker4.setLayoutX(1120);
            num.setRe(Double.parseDouble(cRe.getText()));
            num.setIm(Double.parseDouble(cIm.getText()));
            imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
        });

        cRe.setOnAction(e -> {
            double newRe;
            try{
                newRe = Double.parseDouble(cRe.getText());
            }catch (Exception exception){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Неправильне введене Re!");
                errorAlert.setContentText("Потрібнно ввести число. Наприклад: 3.9, 4.4\nЗначення за замовчуванням - 1.0");
                errorAlert.showAndWait();
                newRe = 1.0;
                cRe.setText("1.0");
            }
            num.setRe(newRe);
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }
        });

        cIm.setOnAction(e -> {
            double newIm;
            try{
                newIm = Double.parseDouble(cIm.getText());
            }catch (Exception exception){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Неправильне введене Im!");
                errorAlert.setContentText("Потрібнно ввести число. Наприклад: 3.9, 4.4\nЗначення за замовчуванням - 1.0");
                errorAlert.showAndWait();
                newIm = 1.0;
                cIm.setText("1.0");
            }
            num.setIm(newIm);
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }
        });

        stepSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            textSlider.setText(String.format("Масштаб: %d%%", newValue.intValue()));
            pxl[0] = 1.0 / Double.parseDouble(newValue.toString());
            if (k3.isSelected()) {
                imageView.setImage(fractionPixelSet3(num, iteration.get(), pxl[0]));
            } else {
                imageView.setImage(fractionPixelSet4(num, iteration.get(), pxl[0]));
            }

        });


    }

}