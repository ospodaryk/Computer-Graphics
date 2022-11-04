package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.effect.Lighting;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;

import static javax.swing.text.html.HTML.Tag.HEAD;
import static javax.swing.text.html.HTML.Tag.P;

public class AFController implements Initializable {

    private static final int CANVAS_WIDTH = 400;
    private static final int CANVAS_HEIGHT = 400;
    private static Integer unit = 100;
    public Pane canvas;
    public Polygon hexagon;
    public Spinner X;
    public Spinner Y;
    public Spinner SIZE;
    public Spinner ANGLE;

    SequentialTransition sequentialTransition = new SequentialTransition();
    @FXML
    private void backToMain() throws IOException {
        App.setRoot("main");
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

        ArrayList<ArrayList<Double>> our_newx_dots = turnDotsWithAngle(0, 0, 1, 0, 150, 113.39);
        canvas.getChildren().remove(hexagon);
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

//        path.getElements().add(new MoveTo(our_newx_dots1.get(0).get(1),our_newx_dots1.get(0).get(1)));
//        path.getElements().add(new LineTo(a,b));

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(2000), hexagon);
        rotateTransition.setByAngle(180f);
        rotateTransition.setCycleCount(4);
        rotateTransition.setAutoReverse(true);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), hexagon);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(2);
        scaleTransition.setToY(2);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(true);

        sequentialTransition.getChildren().addAll(rotateTransition, scaleTransition);
        sequentialTransition.setCycleCount(Timeline.INDEFINITE);
        sequentialTransition.setAutoReverse(true);
        sequentialTransition.play();

    }

    @FXML
    private void stopAnimation() throws InterruptedException {
        sequentialTransition.stop();
    }

    private static ArrayList<ArrayList<Double>> findDots(double centerx, double centery, double koef) {

        System.out.println("======================================");

        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = koef * unit;
        double tmpcentrx = centerx * unit + 200;
        double tmpcentry = centery * unit + 200;
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
        ourdots.forEach(System.out::println);
        System.out.println("======================================");

        return ourdots;
    }

    public static double findx(double X, double Y, double angle) {

        double x = ((X - 300) * (Math.cos(Math.toRadians(angle))) + (Y - 200) * (Math.sin(Math.toRadians(angle)))) + 300;
        return x;
    }

    public static double findy(double X, double Y, double angle) {

        double y = (-1 * (X - 300) * (Math.sin(Math.toRadians(angle))) + (Y - 200) * (Math.cos(Math.toRadians(angle)))) + 200;
        return y;
    }

    private static ArrayList<ArrayList<Double>> turnDotsWithAngle(double centerx, double centery, double koef, double angle, double vx, double vy) {
        System.out.println("======================================");

        System.out.println("FLIPFLAPCHANGE");

        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = koef * unit;
        double tmpcentrx = centerx * unit + 200;
        double tmpcentry = centery * unit + 200;
        double x = 0;
        double y = 0;
        double X = 0;
        double Y = 0;
        ArrayList<Double> v1 = new ArrayList<>();
        X = (tmplength + tmpcentrx);
        Y = (tmpcentry);
        x = findx(X, Y, angle);
        y = findy(X, Y, angle);
        v1.add(x);
        v1.add(y);
        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        X = (tmplength / 2 + tmpcentrx);
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength) + tmpcentry);
        x = findx(X, Y, angle);
        y = findy(X, Y, angle);
        v3.add(x);
        v3.add(y);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        X = Math.abs(tmplength / 2 - tmpcentrx);
        Y = (Math.sqrt(3.0 / 4.0) * tmplength) + tmpcentry;
        ;
        x = findx(X, Y, angle);
        y = findy(X, Y, angle);
        v4.add(x);
        v4.add(y);
        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        X = (Math.abs(tmplength - tmpcentrx));
        Y = (tmpcentry);
        x = findx(X, Y, angle);
        y = findy(X, Y, angle);
        v2.add(x);
        v2.add(y);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        X = (Math.abs(tmplength / 2 - tmpcentrx));
        Y = (Math.abs((Math.sqrt(3.0 / 4.0) * tmplength) - tmpcentry));
        x = findx(X, Y, angle);
        y = findy(X, Y, angle);
        v5.add(x);
        v5.add(y);
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        X = (Math.abs(tmplength / 2 + tmpcentrx));
        Y = (Math.abs((Math.sqrt(3.0 / 4.0) * tmplength) - tmpcentry));
        x = findx(X, Y, angle);
        y = findy(X, Y, angle);
        v6.add(x);
        v6.add(y);
        ourdots.add(v6);
        ourdots.forEach(System.out::println);
        System.out.println("======================================");

        return ourdots;

    }

    public static double findx(double X, double Y, double angle, double centerx, double centery) {

        double x = ((X) * (Math.cos(Math.toRadians(angle))) + (Y) * (Math.sin(Math.toRadians(angle)))) + 200 + centerx * unit;
        return x;
    }

    public static double findy(double X, double Y, double angle, double centerx, double centery) {

        double y = (-1 * (X) * (Math.sin(Math.toRadians(angle))) + (Y) * (Math.cos(Math.toRadians(angle)))) + 200 + centery * unit;
        return y;
    }

    private static ArrayList<ArrayList<Double>> findDotsWithAngle(double centerx, double centery, double koef, double angle) {
        System.out.println("======================================");

        System.out.println("FLIPFLAPCHANGE");

        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = koef * unit;
        double tmpcentrx = centerx * unit;
        double tmpcentry = centery * unit;
        double x = 0;
        double y = 0;
        double X = 0;
        double Y = 0;
        ArrayList<Double> v1 = new ArrayList<>();
        X = (tmplength);
        Y = (0);
        x = findx(X, Y, angle, centerx, centery);
        y = findy(X, Y, angle, centerx, centery);
        v1.add(x);
        v1.add(y);
        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        X = (tmplength / 2);
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        x = findx(X, Y, angle, centerx, centery);
        y = findy(X, Y, angle, centerx, centery);
        v3.add(x);
        v3.add(y);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        x = findx(X, Y, angle, centerx, centery);
        y = findy(X, Y, angle, centerx, centery);
        v4.add(x);
        v4.add(y);

        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        X = ((-tmplength));
        Y = (0);
        x = findx(X, Y, angle, centerx, centery);
        y = findy(X, Y, angle, centerx, centery);
        v2.add(x);
        v2.add(y);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        x = findx(X, Y, angle, centerx, centery);
        y = findy(X, Y, angle, centerx, centery);
        v5.add(x);
        v5.add(y);
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        X = ((tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        x = findx(X, Y, angle, centerx, centery);
        y = findy(X, Y, angle, centerx, centery);
        v6.add(x);
        v6.add(y);
        ourdots.add(v6);
        ourdots.forEach(System.out::println);
        System.out.println("======================================");

        return ourdots;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hexagon = new Polygon();
        hexagon.setFill(new Color(1, 1, 1, 0));
        hexagon.setStroke(new Color(0, 0, 0, 1));
        hexagon.setStrokeWidth(3);

        SpinnerValueFactory<Double> valueFactoryX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-4, 4, 0, 0.1);
        SpinnerValueFactory<Double> valueFactoryY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-4, 4, 0, 0.1);
        SpinnerValueFactory<Double> valueFactorySIZE = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 2, 1, 0.1);
        SpinnerValueFactory<Double> valueFactoryANGLE = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 360, 0, 1);

        X.setValueFactory(valueFactoryX);
        Y.setValueFactory(valueFactoryY);
        SIZE.setValueFactory(valueFactorySIZE);
        ANGLE.setValueFactory(valueFactoryANGLE);

        AtomicReference<Double> centerx = new AtomicReference<>((double) X.getValue());
        AtomicReference<Double> centery = new AtomicReference<>((double) Y.getValue());
        AtomicReference<Double> length = new AtomicReference<>((double) SIZE.getValue());
        AtomicReference<Double> angle = new AtomicReference<>((double) ANGLE.getValue());
//        long cx=Math.round(centerx.get()+200);
//        long cy=Math.round(centerx.get()+200);
        Double cx = (centerx.get() + 200);
        Double cy = (centerx.get() + 200);
        var k = new Polygon();
        k.getPoints().addAll(cx, cy);
        canvas.getChildren().add(k);

        System.out.println("SIZES");
        System.out.println(centerx.get());
        System.out.println(centery.get());
        System.out.println(length.get());
        System.out.println("---------------------------------------------");

        ArrayList<ArrayList<Double>> ourdots = findDotsWithAngle(centerx.get(), centery.get(), length.get(), angle.get());
//        ArrayList<ArrayList<Double>> ourdots = findDots(centerx.get(), centery.get(), length.get());


        hexagon.getPoints().addAll(ourdots.get(0).get(0), ourdots.get(0).get(1),
                ourdots.get(1).get(0), ourdots.get(1).get(1),
                ourdots.get(2).get(0), ourdots.get(2).get(1),
                ourdots.get(3).get(0), ourdots.get(3).get(1),
                ourdots.get(4).get(0), ourdots.get(4).get(1),
                ourdots.get(5).get(0), ourdots.get(5).get(1)
        );

        canvas.getChildren().add(hexagon);


        X.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {

            System.out.println("NEWX:" + newValue);
            centerx.set(newValue);
            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWithAngle(centerx.get(), centery.get(), length.get(), angle.get());
            canvas.getChildren().remove(hexagon);
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
        });

        Y.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {
            System.out.println("NEWX:" + newValue);
            centery.set(newValue);
            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWithAngle(centerx.get(), centery.get(), length.get(), angle.get());
            canvas.getChildren().remove(hexagon);
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

        });

        SIZE.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {
            System.out.println("NEWX:" + newValue);
            length.set(newValue);
            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWithAngle(centerx.get(), centery.get(), length.get(), angle.get());
            canvas.getChildren().remove(hexagon);
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
        });

        ANGLE.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {
            System.out.println("NEWX:" + newValue);

            angle.set(newValue);
//            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWithAngle(centerx.get(), centery.get(), length.get(), angle.get());
            ArrayList<ArrayList<Double>> our_newx_dots = turnDotsWithAngle(centerx.get(), centery.get(), length.get(), angle.get(), 150, 113.39);


            canvas.getChildren().remove(hexagon);
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
        });

    }

    Double getValue(Spinner spinner) {
        return Double.parseDouble(String.valueOf(spinner.getValue()));
    }

}
