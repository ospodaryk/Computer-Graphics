package com.computer_graphic.controllers;

import com.computer_graphic.App;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.awt.*;
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
    boolean CHOOSEHEAD = false;
    @FXML
    public Slider sliderUNIX;
    private static double UNIT = 25;
    private static double KOEF = 1;

    private static Integer CANVAS_SIZE = 250;
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
    public Spinner RESIZE;

    public Spinner ANGLE;
    boolean checkIfMove = false;
    AtomicReference<Double> cx = new AtomicReference<>();
    AtomicReference<Double> centerx = new AtomicReference<>();
    AtomicReference<Double> centery = new AtomicReference<>();
    AtomicReference<Double> length = new AtomicReference<>();
    AtomicReference<Double> angle = new AtomicReference<>();
    AtomicReference<Double> resize = new AtomicReference<>();
    Timeline timeline = new Timeline();
    ArrayList<ArrayList<Double>> working_Dots = new ArrayList<>();
    Path path = new Path();
    MoveTo corner1 = new MoveTo();
    LineTo corner2 = new LineTo();
    LineTo corner3 = new LineTo();
    LineTo corner4 = new LineTo();
    LineTo corner5 = new LineTo();
    LineTo corner6 = new LineTo();

    public ArrayList<ArrayList<Double>> rotateBackExactHeadSystemMatrix(double vx, double vy) {
        vx -= CANVAS_SIZE + centerx.get() * UNIT;
        vy -= CANVAS_SIZE - centery.get() * UNIT;
        ArrayList<ArrayList<Double>> rotatingSystemMatrix = new ArrayList<>();
        ArrayList<Double> firstRow = new ArrayList<>();
        firstRow.add(1.0);
        firstRow.add(0.0);
        firstRow.add(0.0);
        rotatingSystemMatrix.add(firstRow);

        ArrayList<Double> secondRow = new ArrayList<>();
        secondRow.add(0.0);
        secondRow.add(1.0);
        secondRow.add(0.0);
        rotatingSystemMatrix.add(secondRow);


        ArrayList<Double> thirdRow = new ArrayList<>();
        thirdRow.add(vx);
        thirdRow.add(-vy);
        thirdRow.add(1.0);
        rotatingSystemMatrix.add(thirdRow);


        return rotatingSystemMatrix;

    }


    public ArrayList<ArrayList<Double>> rotateExactHeadSystemMatrix(double vx, double vy) {
        vx -= CANVAS_SIZE + centerx.get() * UNIT;
        vy -= CANVAS_SIZE - centery.get() * UNIT;

        ArrayList<ArrayList<Double>> rotatingSystemMatrix = new ArrayList<>();
        ArrayList<Double> firstRow = new ArrayList<>();
        firstRow.add(1.0);
        firstRow.add(0.0);
        firstRow.add(0.0);
        rotatingSystemMatrix.add(firstRow);

        ArrayList<Double> secondRow = new ArrayList<>();
        secondRow.add(0.0);
        secondRow.add(1.0);
        secondRow.add(0.0);
        rotatingSystemMatrix.add(secondRow);


        ArrayList<Double> thirdRow = new ArrayList<>();
        thirdRow.add(-vx);
        thirdRow.add(vy);
        thirdRow.add(1.0);
        rotatingSystemMatrix.add(thirdRow);


        return rotatingSystemMatrix;

    }


    public ArrayList<ArrayList<Double>> rotatingSystemMatrix(double angle) {

        ArrayList<ArrayList<Double>> rotatingSystemMatrix = new ArrayList<>();
        ArrayList<Double> firstRow = new ArrayList<>();
        firstRow.add(Math.cos(Math.toRadians(angle)));
        firstRow.add(Math.sin(Math.toRadians(angle)));
        firstRow.add(0.0);
        rotatingSystemMatrix.add(firstRow);

        ArrayList<Double> secondRow = new ArrayList<>();
        secondRow.add(-(Math.sin(Math.toRadians(angle))));
        secondRow.add(Math.cos(Math.toRadians(angle)));
        secondRow.add(0.0);
        rotatingSystemMatrix.add(secondRow);


        ArrayList<Double> thirdRow = new ArrayList<>();
        thirdRow.add(0.0);
        thirdRow.add(0.0);
        thirdRow.add(1.0);
        rotatingSystemMatrix.add(thirdRow);

        return rotatingSystemMatrix;
    }


    public ArrayList<ArrayList<Double>> resizeSystemMatrix(double index) {

        ArrayList<ArrayList<Double>> rotatingSystemMatrix = new ArrayList<>();
        ArrayList<Double> firstRow = new ArrayList<>();
        firstRow.add((UNIT) * index / KOEF);
        firstRow.add(0.0);
        firstRow.add(0.0);
        rotatingSystemMatrix.add(firstRow);

        ArrayList<Double> secondRow = new ArrayList<>();
        secondRow.add(0.0);
        secondRow.add((UNIT) * index / KOEF);
        secondRow.add(0.0);
        rotatingSystemMatrix.add(secondRow);


        ArrayList<Double> thirdRow = new ArrayList<>();
        thirdRow.add(0.0);
        thirdRow.add(0.0);
        thirdRow.add(1.0);
        rotatingSystemMatrix.add(thirdRow);

        return rotatingSystemMatrix;
    }

    public ArrayList<ArrayList<Double>> moveSystemMatrix(double dx, double dy) {

        ArrayList<ArrayList<Double>> rotatingSystemMatrix = new ArrayList<>();
        ArrayList<Double> firstRow = new ArrayList<>();
        firstRow.add(1.0);
        firstRow.add(0.0);
        firstRow.add(0.0);
        rotatingSystemMatrix.add(firstRow);

        ArrayList<Double> secondRow = new ArrayList<>();
        secondRow.add(0.0);
        secondRow.add(1.0);
        secondRow.add(0.0);
        rotatingSystemMatrix.add(secondRow);


        ArrayList<Double> thirdRow = new ArrayList<>();
        thirdRow.add(dx * UNIT + CANVAS_SIZE);
        thirdRow.add(-dy * UNIT + CANVAS_SIZE);
        thirdRow.add(1.0);
        rotatingSystemMatrix.add(thirdRow);


        return rotatingSystemMatrix;

    }

    @FXML
    private void build() throws InterruptedException {
        centerx.set(0.0);
        centery.set(0.0);
        cx = new AtomicReference<>((double) CX.getValue());
        ArrayList<ArrayList<Double>> newDots = findDotsWithExactHead();
        setHexagon(newDots);
    }

    public Double MapValueToCanvas(double a) {
        return (a * UNIT) + CANVAS_SIZE;
    }

    public Double MapValueFromCanvas(double a) {
        return (a - CANVAS_SIZE) / UNIT;
    }


    private ArrayList<ArrayList<Double>> turnDotsArroundExactHead(double angle, double vx, double vy, double index) {
        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();

        double X = 0;
        double Y = 0;
        ArrayList<Double> v1 = new ArrayList<>();
        X = (length.get());
        Y = (0);
        v1.add(X);
        v1.add(Y);
        v1.add(1.0);

        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        X = (length.get() / 2);
        Y = ((Math.sqrt(3.0 / 4.0) * length.get()));
        v3.add(X);
        v3.add(Y);
        v3.add(1.0);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        X = ((-length.get() / 2));
        Y = ((Math.sqrt(3.0 / 4.0) * length.get()));
        v4.add(X);
        v4.add(Y);
        v4.add(1.0);
        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        X = ((-length.get()));
        Y = (0);
        v2.add(X);
        v2.add(Y);
        v2.add(1.0);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        X = ((-length.get() / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * length.get())));
        v5.add(X);
        v5.add(Y);
        v5.add(1.0);
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        X = ((length.get() / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * length.get())));
        v6.add(X);
        v6.add(Y);
        v6.add(1.0);
        ourdots.add(v6);

        ArrayList<ArrayList<Double>> matrixtoMove = moveSystemMatrix(centerx.get(), centery.get());

        ArrayList<ArrayList<Double>> matrixtoRotate1 = rotateExactHeadSystemMatrix(vx, vy);
        ArrayList<ArrayList<Double>> matrixtoRotate2 = rotatingSystemMatrix(angle);
        ArrayList<ArrayList<Double>> matrixtoRotate3 = rotateBackExactHeadSystemMatrix(vx, vy);

        ArrayList<ArrayList<Double>> matrixtoResize = resizeSystemMatrix(index);

        ourdots = multiplyMatrix(ourdots, matrixtoResize);
        ourdots = multiplyMatrix(ourdots, matrixtoRotate1);
        ourdots = multiplyMatrix(ourdots, matrixtoRotate2);
        ourdots = multiplyMatrix(ourdots, matrixtoRotate3);
        ourdots = multiplyMatrix(ourdots, matrixtoMove);
        return ourdots;

    }

    private ArrayList<ArrayList<Double>> findDotsWhenTurnAroundCenter(double angle, double index) {

        ArrayList<ArrayList<Double>> ourdots = new ArrayList<>();
        double tmplength = length.get();
        double X = 0;
        double Y = 0;
        ArrayList<Double> v1 = new ArrayList<>();
        X = (tmplength);
        Y = (0);
        v1.add(X);
        v1.add(Y);
        v1.add(1.0);

        ourdots.add(v1);

        ArrayList<Double> v3 = new ArrayList<>();
        X = (tmplength / 2);
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        v3.add(X);
        v3.add(Y);
        v3.add(1.0);
        ourdots.add(v3);

        ArrayList<Double> v4 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((Math.sqrt(3.0 / 4.0) * tmplength));
        v4.add(X);
        v4.add(Y);
        v4.add(1.0);
        ourdots.add(v4);

        ArrayList<Double> v2 = new ArrayList<>();
        X = ((-tmplength));
        Y = (0);
        v2.add(X);
        v2.add(Y);
        v2.add(1.0);
        ourdots.add(v2);

        ArrayList<Double> v5 = new ArrayList<>();
        X = ((-tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        v5.add(X);
        v5.add(Y);
        v5.add(1.0);
        ourdots.add(v5);

        ArrayList<Double> v6 = new ArrayList<>();
        X = ((tmplength / 2));
        Y = ((-(Math.sqrt(3.0 / 4.0) * tmplength)));
        v6.add(X);
        v6.add(Y);
        v6.add(1.0);
        ourdots.add(v6);


        ArrayList<ArrayList<Double>> matrixtoMove = moveSystemMatrix(centerx.get(), centery.get());
        ArrayList<ArrayList<Double>> matrixtoRotate = rotatingSystemMatrix(angle);
        ArrayList<ArrayList<Double>> matrixtoResize = resizeSystemMatrix(index);


        ourdots = multiplyMatrix(ourdots, matrixtoResize);
        ourdots = multiplyMatrix(ourdots, matrixtoRotate);
        ourdots = multiplyMatrix(ourdots, matrixtoMove);
        return ourdots;
    }

    static ArrayList<ArrayList<Double>> multiplyMatrix(
            ArrayList<ArrayList<Double>> ourdots,
            ArrayList<ArrayList<Double>> matrix) {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();


        // Check if multiplication is Possible
        if (ourdots.get(0).size() != matrix.size()) {
            System.out.println(
                    "\nMultiplication Not Possible");
            return null;
        }
        int row1 = ourdots.size();
        int row2 = matrix.size();
        int col1 = ourdots.get(0).size();
        int col2 = matrix.get(0).size();
        Double C[][] = new Double[ourdots.size()][ourdots.get(0).size()];

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                C[i][j] = ourdots.get(i).get(0) * matrix.get(0).get(j) + ourdots.get(i).get(1) * matrix.get(1).get(j) + ourdots.get(i).get(2) * matrix.get(2).get(j);
            }
        }

        ArrayList<Double> frow = new ArrayList<>();
        ArrayList<Double> srow = new ArrayList<>();
        ArrayList<Double> trow = new ArrayList<>();
        ArrayList<Double> forow = new ArrayList<>();
        ArrayList<Double> firow = new ArrayList<>();
        ArrayList<Double> sirow = new ArrayList<>();

        for (int k = 0; k < 3; k++) {
            frow.add(C[0][k]);
            srow.add(C[1][k]);
            trow.add(C[2][k]);
            forow.add(C[3][k]);
            firow.add(C[4][k]);
            sirow.add(C[5][k]);
        }

        result.add(frow);
        result.add(srow);
        result.add(trow);
        result.add(forow);
        result.add(firow);
        result.add(sirow);

        return result;
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
        if (!checkIfMove) {
            if ((7 - length.get() * Math.abs(resize.get() + 0.5)) < centerx.get()) {
                centerx.set( (7 - length.get() * Math.abs(resize.get() + 0.5)));
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Координати X були невалідні для системи, шестикутник було переміщено на валідні координати для анімації");
                errorAlert.showAndWait();
            }

            SpinnerValueFactory<Double> valueFactoryX_2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(-7+ length.get() * Math.abs(resize.get() + 0.1), 7 - length.get() * Math.abs(resize.get() + 0.5), centerx.get(), 0.1);
            X.setValueFactory(valueFactoryX_2);
            if ((8 - length.get() * Math.abs(resize.get() + 0.5)) < centery.get()) {
                centery.set( (8 - length.get() * Math.abs(resize.get() + 0.5)));
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Координати Y були невалідні для системи, шестикутник було переміщено на валідні координати для анімації");
                errorAlert.showAndWait();
            }
            SpinnerValueFactory<Double> valueFactoryY_2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(-8+ length.get() * Math.abs(resize.get() + 0.1), 8 - length.get() * Math.abs(resize.get() + 0.5), centery.get(), 0.1);
            Y.setValueFactory(valueFactoryY_2);

            draw();
            checkIfMove = true;
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Анімація вже запущена");
            errorAlert.showAndWait();
        }
    }


    @FXML
    private void stopAnimation() throws InterruptedException {
        checkIfMove = false;
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
        double tmplength = len(corner1.xProperty().get(), corner4.xProperty().get(), corner1.yProperty().get(), corner4.yProperty().get());
        canvas.getChildren().add(hexagon);
        working_Dots.get(0).set(0, corner1.xProperty().get());
        working_Dots.get(0).set(0, corner1.yProperty().get());
        working_Dots.get(1).set(0, corner2.xProperty().get());
        working_Dots.get(1).set(0, corner2.yProperty().get());
        working_Dots.get(2).set(0, corner3.xProperty().get());
        working_Dots.get(2).set(0, corner3.yProperty().get());
        working_Dots.get(3).set(0, corner4.xProperty().get());
        working_Dots.get(3).set(0, corner4.yProperty().get());
        working_Dots.get(4).set(0, corner5.xProperty().get());
        working_Dots.get(4).set(0, corner5.yProperty().get());
        working_Dots.get(5).set(0, corner6.xProperty().get());
        working_Dots.get(5).set(0, corner6.yProperty().get());
        length.set(tmplength / 2 / UNIT * KOEF);


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
        SpinnerValueFactory<Double> valueFactorySIZE = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 5, 3, 0.1);
        SpinnerValueFactory<Double> valueFactoryANGLE = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 360, 0, 1);
        SpinnerValueFactory<Double> valueFactoryResize = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 3, 2, 0.1);

        CX.setValueFactory(valueFactoryCX);
        X.setValueFactory(valueFactoryX);
        Y.setValueFactory(valueFactoryY);
        SIZE.setValueFactory(valueFactorySIZE);
        ANGLE.setValueFactory(valueFactoryANGLE);
        RESIZE.setValueFactory(valueFactoryResize);

        double tmp;
        try{
            tmp = (double) X.getValue();
        }catch(Exception e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Неправильне введене X!");
            errorAlert.setContentText("Потрібнно ввести число. Наприклад: 3, 6\nЗначення за замовчуванням - 0.0");
            errorAlert.showAndWait();
            tmp = 0.0;
            //X.setValueFactory(tmp);
        }

        centerx = new AtomicReference<>(tmp);
        centery = new AtomicReference<>((double) Y.getValue());
        length = new AtomicReference<>((double) SIZE.getValue());
        length.set(3.0);
        angle = new AtomicReference<>((double) ANGLE.getValue());
        resize = new AtomicReference<>((double) RESIZE.getValue());


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

            SpinnerValueFactory<Double> valueFactoryX_2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(-10+length.get(), 10-length.get() , newValue, 0.1);
            X.setValueFactory(valueFactoryX_2);

            centerx.set(newValue);
            working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);
            setHexagon(working_Dots);
            setComboBox(working_Dots);

        });

        Y.valueProperty().

                addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) ->

                {

                    SpinnerValueFactory<Double> valueFactoryY_2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(-10+length.get(), 10-length.get() , newValue, 0.1);
                    Y.setValueFactory(valueFactoryY_2);

                    centery.set(newValue);
                    working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);
                    setComboBox(working_Dots);

                    setHexagon(working_Dots);


                });

        SIZE.valueProperty().

                addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) ->

                {
//            System.out.println("NEWX:" + newValue);

                    length.set(newValue);
                    working_Dots = findDotsWhenTurnAroundCenter(angle.get(), 1);
                    setComboBox(working_Dots);
                    setHexagon(working_Dots);

                });
        RESIZE.valueProperty().

                addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) ->

                {

                    resize.set(newValue);

                });

        ANGLE.valueProperty().

                addListener((ChangeListener<Double>) (observableValue, oldValue, newValue) ->

                {

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
                        working_Dots = turnDotsArroundExactHead(angle.get(), a, b, 1);
                    }
                    TurnComboBox.setValue(tmpp);
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
        sliderUNIX.valueProperty().

                addListener((obs, oldValue, newValue) ->

                {


                    canvas.getChildren().removeAll();
                    hexagon.getPoints().removeAll();
                    hexagon.getPoints().clear();
                    KOEF = newValue.intValue();
                    boolean check = false;

                    if (checkIfMove) {
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
                    if (checkIfMove) {
                        draw();
                    }
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

    @FXML
    private void backToMain() throws IOException {
        App.setRoot("main");
    }

    @FXML
    private void goToTheory() throws IOException {
        App.setRoot("afTheory");
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
//        canvas.getChildren().forEach(System.out::println);


        int stepx = 10;
        for (double i = 0; i <= CANVAS_SIZE * 2; i += UNIT / KOEF) {
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
        for (double i = 0; i <= CANVAS_SIZE * 2; i += UNIT / KOEF) {

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

        double increase = resize.get();


        if (Objects.equals(valueFromComboBox, TurnComboBox.getItems().get(6).toString())) {
            double size = 1;

            double stepSize = (increase - size) / 10;
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
//            size += stepSize;

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
            size -= stepSize;

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


        double seconds = 0.2;
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
        timeline.setAutoReverse(false);
        timeline.setCycleCount(10);
        timeline.play();

    }

    public ArrayList<ArrayList<Double>> findDotsWithExactHead() {
        ArrayList<ArrayList<Double>> sampleDots = findDotsWhenTurnAroundCenter(0, 1);
        double tmpx;
        double tmplength;
        double tmpy;

        tmpx = cx.get() * UNIT + CANVAS_SIZE;
        tmplength = length.get() * UNIT;
        tmpy = (Math.pow((tmplength * tmplength - cx.get() * cx.get() * UNIT * UNIT), 0.5)) + CANVAS_SIZE;

        double tm = (Math.pow((length.get() * length.get() - cx.get() * cx.get()), 0.5));


        if ((cx.get() < 2.5) || ((cx.get() > 0) && (cx.get() < -2.5))) {
            tm *= -1;
        }

        coordinates.setText("( " + cx.get() + " , " + tm + " )");

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
        if (tmpvalue > 1) {
            tmpvalue--;
        }

        double alpa = (Math.acos(tmpvalue));
        System.out.println(Math.toDegrees(alpa));
        ArrayList<ArrayList<Double>> ourdotsXY = findDotsWhenTurnAroundCenter(Math.toDegrees(alpa), 1);
        return ourdotsXY;
    }

    public Double len(Double x1, Double x2, Double y1, Double y2) {
        return Math.pow((Math.pow(x2 - x1, 2) + (Math.pow(y2 - y1, 2))), 0.5);
    }


}




