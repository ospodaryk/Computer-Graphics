package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicReference;

public class AFController implements Initializable {

    @FXML
    public Slider sliderUNIX;
    private static double UNIT = 25;
    private static double KOEF = 1;

    private static Integer CANVAS_SIZE = 250;
    private static Integer UNIT_LENGTH = 20;
    public Text textSlider;
    public Text coordinates;

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 500;
    public Pane canvas;
    public ComboBox TurnComboBox;
    public static String valueFromComboBox;

    public Polygon hexagon;
    public Spinner CX;
    public Spinner X;
    public Spinner Y;
    public Spinner SIZE;
    public Spinner ANGLE;
    AtomicReference<Double> cx = new AtomicReference<>();
    AtomicReference<Double> centerx = new AtomicReference<>();
    AtomicReference<Double> centery = new AtomicReference<>();
    AtomicReference<Double> length = new AtomicReference<>();
    AtomicReference<Double> angle = new AtomicReference<>();
    Timeline timeline = new Timeline();
    ArrayList<ArrayList<Double>> working_Dots = new ArrayList<>();

    Path path = new Path();
    MoveTo corner1 = new MoveTo();
    LineTo corner2 = new LineTo();
    LineTo corner3 = new LineTo();
    LineTo corner4 = new LineTo();
    LineTo corner5 = new LineTo();
    LineTo corner6 = new LineTo();

    @FXML
    private void backToMain() throws IOException {
        App.setRoot("main");
    }

    @FXML
    private void build() throws InterruptedException {
        cx = new AtomicReference<>((double) CX.getValue());
//
        ArrayList<ArrayList<Double>> newDots = findDotsWithExactHead();

        setHexagon(newDots);
//        ArrayList<ArrayList<Double>> ourdotsXY = findDotsWhenTurnAroundCenter(20);
//        setHexagon(ourdotsXY);

    }

    public Double MapValueToCanvas(double a) {
        return (a * UNIT) + CANVAS_SIZE;
    }

    public Double MapValueFromCanvas(double a) {
        return (a - CANVAS_SIZE) / UNIT;
    }

    public ArrayList<ArrayList<Double>> findDotsWithExactHead() {
        ArrayList<ArrayList<Double>> sampleDots = findDots(centerx.get(), centery.get(), length.get());
        double tmpx;
        double tmplength;
        double tmpy;

        if (cx.get() == 4) {
            tmpx = 164.39 + centerx.get();
            tmpy = 300 + centery.get();
            tmplength = length.get() * UNIT_LENGTH;

        } else {
            tmpx = cx.get() * UNIT + CANVAS_SIZE;
            tmplength = length.get() * UNIT_LENGTH;
            tmpy = (Math.pow((length.get() * length.get() * UNIT_LENGTH * UNIT_LENGTH - cx.get() * UNIT * UNIT * cx.get()), 0.5)) + CANVAS_SIZE;
        }
        double tm = (Math.pow((length.get() * length.get() - cx.get() * cx.get()), 0.5));

        if ((cx.get() > 2.5) || ((cx.get() < 0) && (cx.get() > -2.5))) {
            tm *= -1;
        }
        coordinates.setText("( " + cx.get() + " , " + Math.round(tm) + " )");
        System.out.println("( " + (tmpx) + " , " + (tmpy) + " )");

        Double min = len(tmpx, tmpy, sampleDots.get(0).get(0), sampleDots.get(0).get(1));

        Double x2 = sampleDots.get(0).get(0);
        Double y2 = sampleDots.get(0).get(1);
        for (int i = 0; i < sampleDots.size(); i++) {
            Double tmp = min;
            min = len(tmpx, tmpy, sampleDots.get(i).get(0), sampleDots.get(i).get(1));
            if (tmp < min) {
                min = tmp;
                x2 = sampleDots.get(i).get(0);
                y2 = sampleDots.get(i).get(1);
            }

        }
        double tmpvalue = (Math.abs((tmpx - CANVAS_SIZE) * (x2 - CANVAS_SIZE)) + (Math.abs(tmpy - CANVAS_SIZE) * Math.abs(y2 - CANVAS_SIZE))) / Math.pow(tmplength, 2);
        double alpa = (Math.acos(tmpvalue));
        System.out.println(Math.toDegrees(alpa));
        ArrayList<ArrayList<Double>> ourdotsXY = findDotsWhenTurnAroundCenter(Math.toDegrees(alpa), 1);
        return ourdotsXY;
    }

    public Double len(Double x1, Double x2, Double y1, Double y2) {


        return Math.pow((Math.pow(x2 - x1, 2) + (Math.pow(y2 - y1, 2))), 0.5);
    }

    private static ArrayList<ArrayList<Double>> findDots(double centerx, double centery, double koef) {

        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = koef * UNIT_LENGTH;
        double tmpcentrx = centerx * UNIT + CANVAS_SIZE;
        double tmpcentry = centery * UNIT + CANVAS_SIZE;
        ArrayList<Double> v1 = new ArrayList<>();
        v1.add(tmplength + tmpcentrx);
        v1.add(tmpcentry);
        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        v3.add(tmplength / 2 + tmpcentrx);
        v3.add((Math.sqrt(3.0 / 4.0) * tmplength) + tmpcentry);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        v4.add(Math.abs(tmplength / 2 - tmpcentrx));
        v4.add((Math.sqrt(3.0 / 4.0) * tmplength) + tmpcentry);
        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        v2.add(Math.abs(tmplength - tmpcentrx));
        v2.add(tmpcentry);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        v5.add(Math.abs(tmplength / 2 - tmpcentrx));
        v5.add(Math.abs((Math.sqrt(3.0 / 4.0) * tmplength) - tmpcentry));
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        v6.add(Math.abs(tmplength / 2 + tmpcentrx));
        v6.add(Math.abs((Math.sqrt(3.0 / 4.0) * tmplength) - tmpcentry));
        ourdots.add(v6);

        return ourdots;
    }

    public static double findx(double X, double Y, double angle, double vx, double vy) {

        double x = ((X - vx) * (Math.cos(Math.toRadians(angle))) + (Y - vy) * (Math.sin(Math.toRadians(angle)))) + vx;
        return x;
    }

    public static double findy(double X, double Y, double angle, double vx, double vy) {

        double y = (-1 * (X - vx) * (Math.sin(Math.toRadians(angle))) + (Y - vy) * (Math.cos(Math.toRadians(angle)))) + vy;
        return y;
    }

    private ArrayList<ArrayList<Double>> turnDotsArroundExactHead(double angle, double vx, double vy, double index) {


        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = length.get() * UNIT * index;

        double tmpcentrx = centerx.get() * UNIT * index + CANVAS_SIZE;
        double tmpcentry = centery.get() * UNIT * index + CANVAS_SIZE;

        double x = 0;
        double y = 0;
        double X = 0;
        double Y = 0;
        ArrayList<Double> v1 = new ArrayList<>();
        X = (tmplength + tmpcentrx);
        Y = (tmpcentry);
        x = findx(X, Y, angle, vx, vy);
        y = findy(X, Y, angle, vx, vy);
        v1.add(x);
        v1.add(y);
        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        X = (tmplength / 2 + tmpcentrx);
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength) + tmpcentry);
        x = findx(X, Y, angle, vx, vy);
        y = findy(X, Y, angle, vx, vy);
        v3.add(x);
        v3.add(y);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        X = Math.abs(tmplength / 2 - tmpcentrx);
        Y = (Math.sqrt(3.0 / 4.0) * tmplength) + tmpcentry;
        x = findx(X, Y, angle, vx, vy);
        y = findy(X, Y, angle, vx, vy);
        v4.add(x);
        v4.add(y);
        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        X = (Math.abs(tmplength - tmpcentrx));
        Y = (tmpcentry);
        x = findx(X, Y, angle, vx, vy);
        y = findy(X, Y, angle, vx, vy);
        v2.add(x);
        v2.add(y);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        X = (Math.abs(tmplength / 2 - tmpcentrx));
        Y = (Math.abs((Math.sqrt(3.0 / 4.0) * tmplength) - tmpcentry));
        x = findx(X, Y, angle, vx, vy);
        y = findy(X, Y, angle, vx, vy);
        v5.add(x);
        v5.add(y);
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        X = (Math.abs(tmplength / 2 + tmpcentrx));
        Y = (Math.abs((Math.sqrt(3.0 / 4.0) * tmplength) - tmpcentry));
        x = findx(X, Y, angle, vx, vy);
        y = findy(X, Y, angle, vx, vy);
        v6.add(x);
        v6.add(y);
        ourdots.add(v6);

        return ourdots;

    }

    public double fx_TurnAroundCenter(double X, double Y, double angle) {
        double x = ((X) * (Math.cos(Math.toRadians(angle))) + (Y) * (Math.sin(Math.toRadians(angle)))) + CANVAS_SIZE + centerx.get() * UNIT;
        return x;
    }

    public double fy_TurnAroundCenter(double X, double Y, double angle) {

        double y = (-1 * (X) * (Math.sin(Math.toRadians(angle))) + (Y) * (Math.cos(Math.toRadians(angle)))) + CANVAS_SIZE + centery.get() * UNIT;
        return y;
    }

    private ArrayList<ArrayList<Double>> findDotsWhenTurnAroundCenter(double angle, double index) {
        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = length.get() * UNIT_LENGTH * index / KOEF;
        double tmpcentrx = centerx.get() * UNIT * index + CANVAS_SIZE;
        double tmpcentry = centery.get() * UNIT * index + CANVAS_SIZE;
        double x = 0;
        double y = 0;
        double X = 0;
        double Y = 0;
        ArrayList<Double> v1 = new ArrayList<>();
        X = (tmplength);
        Y = (0);
        x = fx_TurnAroundCenter(X, Y, angle);
        y = fy_TurnAroundCenter(X, Y, angle);
        v1.add(x);
        v1.add(y);
        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        X = (tmplength / 2);
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        x = fx_TurnAroundCenter(X, Y, angle);
        y = fy_TurnAroundCenter(X, Y, angle);
        v3.add(x);
        v3.add(y);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        x = fx_TurnAroundCenter(X, Y, angle);
        y = fy_TurnAroundCenter(X, Y, angle);
        v4.add(x);
        v4.add(y);

        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        X = ((-tmplength));
        Y = (0);
        x = fx_TurnAroundCenter(X, Y, angle);
        y = fy_TurnAroundCenter(X, Y, angle);
        v2.add(x);
        v2.add(y);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        x = fx_TurnAroundCenter(X, Y, angle);
        y = fy_TurnAroundCenter(X, Y, angle);
        v5.add(x);
        v5.add(y);
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        X = ((tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        x = fx_TurnAroundCenter(X, Y, angle);
        y = fy_TurnAroundCenter(X, Y, angle);
        v6.add(x);
        v6.add(y);
        ourdots.add(v6);

        return ourdots;
    }


    @FXML
    private void saveCanvasToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void startAnimation() throws InterruptedException {
        draw();
    }

    public void draw() {

        ArrayList<ArrayList<Double>> our_newx_dots = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot2 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot3 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot4 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot5 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot6 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot7 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot8 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot9 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot10 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot11 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot12 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot13 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot14 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot15 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot16 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot17 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot18 = new ArrayList<>();
        ArrayList<ArrayList<Double>> our_newx_dot19 = new ArrayList<>();


        String str = TurnComboBox.getValue().toString();
        int start = str.indexOf("(");
        int end = str.indexOf(")");
        String outStr = str.substring(start + 2, end - 1);
        System.out.println(outStr);
        String[] numbers = outStr.split(" , ");


        int startangle = 0;
        int step = 20;
        Double a = MapValueToCanvas(Double.parseDouble(numbers[0]));
        Double b = MapValueToCanvas(Double.parseDouble(numbers[1]));
        int i_index = 0;
        for (int i = 0; i < working_Dots.size(); i++) {
            if ((Objects.equals(a, working_Dots.get(i).get(0))) && (Objects.equals(b, working_Dots.get(i).get(1)))) {
                i_index = i;
            }

        }
        System.out.println("n0 " + numbers[0] + "n1 " + numbers[1]);

        System.out.println("a " + a + "b " + b);
        working_Dots.forEach(System.out::println);
        if (Objects.equals(valueFromComboBox, TurnComboBox.getItems().get(6).toString())) {
            double size = 1.0;
            double stepSize = 0.05;
            our_newx_dots = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot2 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot3 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot4 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot5 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot6 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot7 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot8 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot9 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot10 = findDotsWhenTurnAroundCenter(startangle, size);
            size += stepSize;
            startangle += step;
            our_newx_dot11 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot12 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot13 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot14 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot15 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot16 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot17 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot18 = findDotsWhenTurnAroundCenter(startangle, size);
            size -= stepSize;
            startangle += step;
            our_newx_dot19 = findDotsWhenTurnAroundCenter(startangle, size);
            startangle += step;


        } else {
            double size = 1.0;
            double stepSize = 0.05;
            our_newx_dots = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot2 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot3 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot4 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot5 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot6 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot7 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot8 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot9 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size += stepSize;

            startangle += step;
            our_newx_dot10 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot11 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot12 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot13 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot14 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot15 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);

            startangle += step;
            our_newx_dot16 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot17 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot18 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
            size -= stepSize;

            startangle += step;
            our_newx_dot19 = turnDotsArroundExactHead(startangle, working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), size);
        }

        canvas.getChildren().remove(hexagon);
        canvas.getChildren().remove(path);
        corner1.xProperty().set(hexagon.getPoints().get(0));
        corner1.yProperty().set(hexagon.getPoints().get(1));
        corner2.xProperty().set(hexagon.getPoints().get(2));
        corner2.yProperty().set(hexagon.getPoints().get(3));
        corner3.xProperty().set(hexagon.getPoints().get(4));
        corner3.yProperty().set(hexagon.getPoints().get(5));
        corner4.xProperty().set(hexagon.getPoints().get(6));
        corner4.yProperty().set(hexagon.getPoints().get(7));
        corner5.xProperty().set(hexagon.getPoints().get(8));
        corner5.yProperty().set(hexagon.getPoints().get(9));
        corner6.xProperty().set(hexagon.getPoints().get(10));
        corner6.yProperty().set(hexagon.getPoints().get(11));
        path = new Path(corner1, corner2, corner3, corner4, corner5, corner6, new ClosePath());
        canvas.getChildren().add(path);


        double seconds = 0.1;
        Timeline timeline2 = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(corner1.xProperty(), our_newx_dots.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dots.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dots.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dots.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dots.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dots.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dots.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dots.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dots.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dots.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dots.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dots.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(corner1.xProperty(), our_newx_dot2.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot2.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot2.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot2.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot2.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot2.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot2.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot2.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot2.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot2.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot2.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot2.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 2),
                        new KeyValue(corner1.xProperty(), our_newx_dot3.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot3.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot3.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot3.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot3.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot3.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot3.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot3.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot3.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot3.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot3.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot3.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 3),
                        new KeyValue(corner1.xProperty(), our_newx_dot4.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot4.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot4.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot4.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot4.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot4.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot4.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot4.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot4.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot4.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot4.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot4.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 4),
                        new KeyValue(corner1.xProperty(), our_newx_dot5.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot5.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot5.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot5.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot5.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot5.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot5.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot5.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot5.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot5.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot5.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot5.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 5),
                        new KeyValue(corner1.xProperty(), our_newx_dot6.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot6.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot6.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot6.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot6.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot6.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot6.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot6.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot6.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot6.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot6.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot6.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 6),
                        new KeyValue(corner1.xProperty(), our_newx_dot7.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot7.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot7.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot7.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot7.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot7.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot7.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot7.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot7.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot7.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot7.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot7.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 7),
                        new KeyValue(corner1.xProperty(), our_newx_dot8.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot8.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot8.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot8.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot8.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot8.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot8.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot8.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot8.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot8.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot8.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot8.get(5).get(1)))
                ,
                new KeyFrame(Duration.seconds(seconds * 8),
                        new KeyValue(corner1.xProperty(), our_newx_dot9.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot9.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot9.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot9.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot9.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot9.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot9.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot9.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot9.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot9.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot9.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot9.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 9),
                        new KeyValue(corner1.xProperty(), our_newx_dot10.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot10.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot10.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot10.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot10.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot10.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot10.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot10.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot10.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot10.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot10.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot10.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 10),
                        new KeyValue(corner1.xProperty(), our_newx_dot11.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot11.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot11.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot11.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot11.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot11.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot11.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot11.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot11.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot11.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot11.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot11.get(5).get(1)))
                ,
                new KeyFrame(Duration.seconds(seconds * 11),
                        new KeyValue(corner1.xProperty(), our_newx_dot12.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot12.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot12.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot12.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot12.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot12.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot12.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot12.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot12.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot12.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot12.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot12.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 12),
                        new KeyValue(corner1.xProperty(), our_newx_dot13.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot13.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot13.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot13.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot13.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot13.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot13.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot13.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot13.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot13.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot13.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot13.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 13),
                        new KeyValue(corner1.xProperty(), our_newx_dot14.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot14.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot14.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot14.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot14.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot14.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot14.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot14.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot14.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot14.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot14.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot14.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 14),
                        new KeyValue(corner1.xProperty(), our_newx_dot15.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot15.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot15.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot15.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot15.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot15.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot15.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot15.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot15.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot15.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot15.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot15.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 15),
                        new KeyValue(corner1.xProperty(), our_newx_dot16.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot16.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot16.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot16.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot16.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot16.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot16.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot16.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot16.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot16.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot16.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot16.get(5).get(1)))
                ,

                new KeyFrame(Duration.seconds(seconds * 16),
                        new KeyValue(corner1.xProperty(), our_newx_dot17.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot17.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot17.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot17.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot17.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot17.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot17.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot17.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot17.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot17.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot17.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot17.get(5).get(1)))
                ,
                new KeyFrame(Duration.seconds(seconds * 17),
                        new KeyValue(corner1.xProperty(), our_newx_dot18.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot18.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot18.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot18.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot18.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot18.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot18.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot18.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot18.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot18.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot18.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot18.get(5).get(1))),
                new KeyFrame(Duration.seconds(seconds * 18),
                        new KeyValue(corner1.xProperty(), our_newx_dot19.get(0).get(0)),
                        new KeyValue(corner1.yProperty(), our_newx_dot19.get(0).get(1)),
                        new KeyValue(corner2.xProperty(), our_newx_dot19.get(1).get(0)),
                        new KeyValue(corner2.yProperty(), our_newx_dot19.get(1).get(1)),
                        new KeyValue(corner3.xProperty(), our_newx_dot19.get(2).get(0)),
                        new KeyValue(corner3.yProperty(), our_newx_dot19.get(2).get(1)),
                        new KeyValue(corner4.xProperty(), our_newx_dot19.get(3).get(0)),
                        new KeyValue(corner4.yProperty(), our_newx_dot19.get(3).get(1)),
                        new KeyValue(corner5.xProperty(), our_newx_dot19.get(4).get(0)),
                        new KeyValue(corner5.yProperty(), our_newx_dot19.get(4).get(1)),
                        new KeyValue(corner6.xProperty(), our_newx_dot19.get(5).get(0)),
                        new KeyValue(corner6.yProperty(), our_newx_dot19.get(5).get(1)))
        );
        timeline = timeline2;
        timeline.setAutoReverse(true);
        timeline.setCycleCount(10);
        timeline.play();

    }

    @FXML
    private void stopAnimation() throws InterruptedException {
        //добавти чек
        timeline.stop();
        hexagon.getPoints().removeAll();
        hexagon.getPoints().clear();
        hexagon.getPoints().addAll(corner1.xProperty().get(), corner1.yProperty().get(),
                corner2.xProperty().get(), corner2.yProperty().get(),
                corner3.xProperty().get(), corner3.yProperty().get(),
                corner4.xProperty().get(), corner4.yProperty().get(),
                corner5.xProperty().get(), corner5.yProperty().get(),
                corner6.xProperty().get(), corner6.yProperty().get()
        );
        canvas.getChildren().add(hexagon);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawLines();
        TurnComboBox.setOnAction(event -> {
            valueFromComboBox = (String) TurnComboBox.getValue();
            System.out.println(valueFromComboBox);
        });
        int t = 22;

        hexagon = new Polygon();
        hexagon.setFill(new Color(1, 1, 1, 0));
        hexagon.setStroke(new Color(0, 0, 0, 1));
        hexagon.setStrokeWidth(3);

        SpinnerValueFactory<Double> valueFactoryCX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-20, 20, 0, 0.1);
        SpinnerValueFactory<Double> valueFactoryX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-20, 20, 0, 0.1);
        SpinnerValueFactory<Double> valueFactoryY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-20, 20, 0, 0.1);
        SpinnerValueFactory<Double> valueFactorySIZE = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 20, 5, 0.1);
        SpinnerValueFactory<Double> valueFactoryANGLE = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 360, 0, 1);

        CX.setValueFactory(valueFactoryCX);
        X.setValueFactory(valueFactoryX);
        Y.setValueFactory(valueFactoryY);
        SIZE.setValueFactory(valueFactorySIZE);
        ANGLE.setValueFactory(valueFactoryANGLE);

        centerx = new AtomicReference<>((double) X.getValue());
        centery = new AtomicReference<>((double) Y.getValue());
        length = new AtomicReference<>((double) SIZE.getValue());
        angle = new AtomicReference<>((double) ANGLE.getValue());


        working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);


        hexagon.getPoints().addAll(working_Dots.get(0).get(0), working_Dots.get(0).get(1),
                working_Dots.get(1).get(0), working_Dots.get(1).get(1),
                working_Dots.get(2).get(0), working_Dots.get(2).get(1),
                working_Dots.get(3).get(0), working_Dots.get(3).get(1),
                working_Dots.get(4).get(0), working_Dots.get(4).get(1),
                working_Dots.get(5).get(0), working_Dots.get(5).get(1)
        );

        canvas.getChildren().add(hexagon);

        setComboBox(working_Dots);

        X.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {

            System.out.println("NEWX:" + newValue);
            centerx.set(newValue);
            working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);
            setHexagon(working_Dots);
            setComboBox(working_Dots);

        });

        Y.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {
            System.out.println("NEWX:" + newValue);
            centery.set(newValue);
            working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);
            setComboBox(working_Dots);

            setHexagon(working_Dots);


        });

        SIZE.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {
            System.out.println("NEWX:" + newValue);

            length.set(newValue);
            working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);
            setComboBox(working_Dots);
            setHexagon(working_Dots);

        });

        ANGLE.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {

            angle.set(newValue);
            String str = TurnComboBox.getValue().toString();
            int start = str.indexOf("(");
            int end = str.indexOf(")");
            String outStr = str.substring(start + 2, end - 1);
            System.out.println(outStr);
            String[] numbers = outStr.split(" , ");

            Double a = MapValueToCanvas(Double.parseDouble(numbers[0]));
            Double b = MapValueToCanvas(Double.parseDouble(numbers[1]));
            int i_index = 0;
            for (int i = 0; i < working_Dots.size(); i++) {
                if ((Objects.equals(a, working_Dots.get(i).get(0))) && (Objects.equals(b, working_Dots.get(i).get(1)))) {
                    i_index = i;
                }

            }


            String tmpp = TurnComboBox.getValue().toString();
            if (Objects.equals(valueFromComboBox, TurnComboBox.getItems().get(6).toString())) {
                working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);
            } else {
                working_Dots = turnDotsArroundExactHead(angle.get(), working_Dots.get(i_index).get(0), working_Dots.get(i_index).get(1), 1);
            }
            setComboBox(working_Dots);
            TurnComboBox.setValue(tmpp);
            setHexagon(working_Dots);

        });
        sliderUNIX.setBlockIncrement(1);
        sliderUNIX.setMax(4);
        sliderUNIX.setMin(1);
        sliderUNIX.setValue(1);
        sliderUNIX.setMajorTickUnit(1);
        sliderUNIX.setMinorTickCount(1);
        sliderUNIX.valueProperty().addListener((obs, oldValue, newValue) -> {


            canvas.getChildren().removeAll();
            hexagon.getPoints().removeAll();
            hexagon.getPoints().clear();
            KOEF = newValue.intValue();
            boolean check = false;

            if (timeline.isAutoReverse()) {
                timeline.stop();
                hexagon.getPoints().removeAll();
                hexagon.getPoints().clear();
                hexagon.getPoints().addAll(corner1.xProperty().get(), corner1.yProperty().get(),
                        corner2.xProperty().get(), corner2.yProperty().get(),
                        corner3.xProperty().get(), corner3.yProperty().get(),
                        corner4.xProperty().get(), corner4.yProperty().get(),
                        corner5.xProperty().get(), corner5.yProperty().get(),
                        corner6.xProperty().get(), corner6.yProperty().get()
                );
                canvas.getChildren().add(hexagon);
                check = true;
            }
            sliderUNIX.setValue(newValue.intValue());


            drawLines();
            textSlider.setText(String.format("%d", newValue.intValue()));
            working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);

            hexagon.getPoints().addAll(working_Dots.get(0).get(0), working_Dots.get(0).get(1),
                    working_Dots.get(1).get(0), working_Dots.get(1).get(1),
                    working_Dots.get(2).get(0), working_Dots.get(2).get(1),
                    working_Dots.get(3).get(0), working_Dots.get(3).get(1),
                    working_Dots.get(4).get(0), working_Dots.get(4).get(1),
                    working_Dots.get(5).get(0), working_Dots.get(5).get(1)
            );

            canvas.getChildren().add(hexagon);
            if (check) {
                draw();
            }
//            try {
//                startAnimation();
//            } catch (InterruptedException e) {
//                System.out.println("ТТТТТТУУУУУТТТ");
//                throw new RuntimeException(e);
//            }
        });

    }

    public void setComboBox(ArrayList<ArrayList<Double>> our_newx_dots) {

        TurnComboBox.getItems().clear();
        ObservableList<String> ourlist = FXCollections.observableArrayList("Вершини ( " + MapValueFromCanvas(our_newx_dots.get(0).get(0)).toString() + " , " + MapValueFromCanvas(our_newx_dots.get(0).get(1)).toString() + " )",
                "Вершини ( " + MapValueFromCanvas(our_newx_dots.get(1).get(0)).toString() + " , " + MapValueFromCanvas(our_newx_dots.get(1).get(1)).toString() + " )",
                "Вершини ( " + MapValueFromCanvas(our_newx_dots.get(2).get(0)).toString() + " , " + MapValueFromCanvas(our_newx_dots.get(2).get(1)).toString() + " )",
                "Вершини ( " + MapValueFromCanvas(our_newx_dots.get(3).get(0)).toString() + " , " + MapValueFromCanvas(our_newx_dots.get(3).get(1)).toString() + " )",
                "Вершини ( " + MapValueFromCanvas(our_newx_dots.get(4).get(0)).toString() + " , " + MapValueFromCanvas(our_newx_dots.get(4).get(1)).toString() + " )",
                "Вершини ( " + MapValueFromCanvas(our_newx_dots.get(5).get(0)).toString() + " , " + MapValueFromCanvas(our_newx_dots.get(5).get(1)).toString() + " )",
                "Центру ( " + centerx.get() + " , " + centery.get() + " )"
        );
        TurnComboBox.setItems(ourlist);


    }


    public void setHexagon(ArrayList<ArrayList<Double>> our_newx_dots) {
        canvas.getChildren().remove(hexagon);
        canvas.getChildren().remove(path);
        hexagon.getPoints().removeAll();
        hexagon.getPoints().clear();
        hexagon.getPoints().addAll(our_newx_dots.get(0).get(0), our_newx_dots.get(0).get(1),
                our_newx_dots.get(1).get(0), our_newx_dots.get(1).get(1),
                our_newx_dots.get(2).get(0), our_newx_dots.get(2).get(1),
                our_newx_dots.get(3).get(0), our_newx_dots.get(3).get(1),
                our_newx_dots.get(4).get(0), our_newx_dots.get(4).get(1),
                our_newx_dots.get(5).get(0), our_newx_dots.get(5).get(1)
        );
        canvas.getChildren().add(hexagon);
    }

    Double getValue(Spinner spinner) {
        return Double.parseDouble(String.valueOf(spinner.getValue()));
    }


    public void drawLines() {
        canvas.getChildren().clear();
//        canvas.getChildren().add(hexagon);
        canvas.getChildren().forEach(System.out::println);


        int stepx = 10;
        for (double i = 0; i <= CANVAS_SIZE * 2; i += UNIT/KOEF) {
            if (i == CANVAS_SIZE) {
                Polygon ox = new Polygon(i, 0, i, CANVAS_SIZE * 2);
                ox.setFill(new Color(1, 1, 1, 0));
                ox.setStroke(new Color(0, 0, 0, 1));
                ox.setStrokeWidth(2);
                canvas.getChildren().add(ox);

            }
            Polygon line = new Polygon(i, 0, i, CANVAS_SIZE * 2);
            line.setFill(Color.GRAY);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);
            canvas.getChildren().add(line);
        }
        for (double i = 0; i <= CANVAS_SIZE * 2; i += UNIT/KOEF) {

            if (i == CANVAS_SIZE) {

                Polygon ox = new Polygon(0, i, CANVAS_SIZE * 2, i);
                ox.setFill(new Color(1, 1, 1, 0));
                ox.setStroke(new Color(0, 0, 0, 1));
                ox.setStrokeWidth(2);
                canvas.getChildren().add(ox);

            }
            Polygon line = new Polygon(0, i, CANVAS_SIZE * 2, i);
            line.setFill(Color.GRAY);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);
            canvas.getChildren().add(line);
        }


    }
}



