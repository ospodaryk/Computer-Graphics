package com.computer_graphic.controllers;

import com.computer_graphic.App;
import com.computer_graphic.model.colors.RgbColor;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.effect.Lighting;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;
import org.w3c.dom.Node;

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

import static javax.swing.text.html.HTML.Tag.*;

public class AFController implements Initializable {
    private static Integer UNIT = 20;
    private static Integer CANVAS_SIZE = 250;
    private static Integer UNIT_LENGTH = 20;
    public Text coordinates;

    private static final int CANVAS_WIDTH = 400;
    private static final int CANVAS_HEIGHT = 400;
    public Pane canvas;
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
        double tm=(Math.pow((length.get() * length.get()  - cx.get()  * cx.get()), 0.5));

        if((cx.get()>2.5)||((cx.get()<0)&&(cx.get()>-2.5))){
            tm*=-1;
        }
        coordinates.setText("( " + cx.get()+ " , " + Math.round(tm) + " )");
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
        ArrayList<ArrayList<Double>> ourdotsXY = findDotsWhenTurnAroundCenter(Math.toDegrees(alpa));
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
        double tmplength = koef * UNIT_LENGTH;
        double tmpcentrx = centerx * UNIT + CANVAS_SIZE;
        double tmpcentry = centery * UNIT + CANVAS_SIZE;
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

    public static double fx_TurnAroundCenter(double X, double Y, double angle, double centerx, double centery) {

        double x = ((X) * (Math.cos(Math.toRadians(angle))) + (Y) * (Math.sin(Math.toRadians(angle)))) + CANVAS_SIZE + centerx * UNIT;
        return x;
    }

    public static double fy_TurnAroundCenter(double X, double Y, double angle, double centerx, double centery) {

        double y = (-1 * (X) * (Math.sin(Math.toRadians(angle))) + (Y) * (Math.cos(Math.toRadians(angle)))) + CANVAS_SIZE + centery * UNIT;
        return y;
    }

    private ArrayList<ArrayList<Double>> findDotsWhenTurnAroundCenter(double angle) {
        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = length.get() * UNIT_LENGTH;
        double tmpcentrx = centerx.get() * UNIT + CANVAS_SIZE;
        double tmpcentry = centery.get() * UNIT + CANVAS_SIZE;
        double x = 0;
        double y = 0;
        double X = 0;
        double Y = 0;
        ArrayList<Double> v1 = new ArrayList<>();
        X = (tmplength);
        Y = (0);
        x = fx_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        y = fy_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        v1.add(x);
        v1.add(y);
        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        X = (tmplength / 2);
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        x = fx_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        y = fy_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        v3.add(x);
        v3.add(y);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        x = fx_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        y = fy_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        v4.add(x);
        v4.add(y);

        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        X = ((-tmplength));
        Y = (0);
        x = fx_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        y = fy_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        v2.add(x);
        v2.add(y);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        x = fx_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        y = fy_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        v5.add(x);
        v5.add(y);
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        X = ((tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        x = fx_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        y = fy_TurnAroundCenter(X, Y, angle, centerx.get(), centery.get());
        v6.add(x);
        v6.add(y);
        ourdots.add(v6);
        System.out.println("==============ANGLE===============");
        System.out.println("--" + angle + "--");
        ourdots.forEach(System.out::println);
        System.out.println("======================================");

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

        ArrayList<ArrayList<Double>> our_newx_dots = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), 0, 150, 113.39);
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
        int startangle = 0;
        int step = 10;
        ArrayList<ArrayList<Double>> our_newx_dot2 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 2 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot3 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 4 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot4 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 6 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot5 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 8 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot6 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 10 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot7 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 12 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot8 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 14 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot9 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 16 * step, 150, 113.39);
        ArrayList<ArrayList<Double>> our_newx_dot10 = turnDotsWithAngle(centerx.get(), centerx.get(), length.get(), startangle + 18 * step, 150, 113.39);

        int seconds = 1;
        timeline = new Timeline(
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
                        new KeyValue(corner6.yProperty(), our_newx_dot10.get(5).get(1)))


        );
        timeline.setAutoReverse(true);
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
        int t = 22;

        hexagon = new Polygon();
        hexagon.setFill(new Color(1, 1, 1, 0));
        hexagon.setStroke(new Color(0, 0, 0, 1));
        hexagon.setStrokeWidth(3);

        SpinnerValueFactory<Double> valueFactoryCX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-20, 20, 0, 0.1);
        SpinnerValueFactory<Double> valueFactoryX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-20, 20, 0, 0.1);
        SpinnerValueFactory<Double> valueFactoryY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-20, 20, 0, 0.1);
        SpinnerValueFactory<Double> valueFactorySIZE = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 20, 10, 0.1);
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


        ArrayList<ArrayList<Double>> ourdots = findDotsWhenTurnAroundCenter(angle.get());


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
            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWhenTurnAroundCenter(angle.get());
            setHexagon(our_newx_dots);

        });

        Y.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {
            System.out.println("NEWX:" + newValue);
            centery.set(newValue);
            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWhenTurnAroundCenter(angle.get());
            setHexagon(our_newx_dots);


        });

        SIZE.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {
            System.out.println("NEWX:" + newValue);
            length.set(newValue);
            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWhenTurnAroundCenter(angle.get());
            setHexagon(our_newx_dots);

        });

        ANGLE.valueProperty().addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) -> {

            angle.set(newValue);
            ArrayList<ArrayList<Double>> our_newx_dots = findDotsWhenTurnAroundCenter(angle.get());
            setHexagon(our_newx_dots);

        });

    }

    public void formulaCorner(int x, int y) {


    }

    public void findHEcagonByOneCorner(int x, int y) {


    }

    public void setHexagon(ArrayList<ArrayList<Double>> our_newx_dots) {
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
    }

    Double getValue(Spinner spinner) {
        return Double.parseDouble(String.valueOf(spinner.getValue()));
    }

}



