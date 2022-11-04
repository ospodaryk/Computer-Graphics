package com.computer_graphic.controllers;

import com.computer_graphic.App;
import com.computer_graphic.model.colors.HslColor;
import com.computer_graphic.model.colors.RgbColor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Light;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;

public class ColorsController implements Initializable {

    public static Rectangle rectangle;

    static {
        rectangle = new Rectangle(0, 0);
    }

    @FXML
    public Slider lightnessSlider;
    public Canvas colorCanvas;
    public Canvas rectangleCanvas;
    public Text hslLine;
    public Text rgbLine;
    public ImageView imageOriginal;
    public ImageView imageView;
    public Text lightnessText;
    public Image image;
    public ComboBox colorsComboBox;
    public static String colorFromComboBox;

    @FXML
    private void backToMain() throws IOException {
        App.setRoot("colors");
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(new Stage());
        image = new Image(file.toURI().toString());
        imageOriginal.setImage(image);
//        imageView.setImage(image);
        imageView.setImage(reColor(image, 1.0 / 100, colorCanvas));
    }

    @FXML
    private void saveCanvasToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.snapshot(null, null), null);
                ImageIO.write(bImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static double mapValue(double value, double min, double max){
        return value < min ? min : Math.min(value, max);
    }
    private static WritableImage fillImage(Image inputImage, WritableImage outputImage, double lightness){
        int inputImageWidth = (int) inputImage.getWidth();
        int inputImageHeight = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();

        PixelWriter writer = outputImage.getPixelWriter();

        for (int y = 0; y < inputImageHeight; y++) {
            for (int x = 0; x < inputImageWidth; x++) {
                var color = reader.getColor(x, y);
                HslColor hslColor = new RgbColor(color.getRed(), color.getGreen(), color.getBlue()).toHsl();
                hslColor.l = mapValue(hslColor.l + lightness, 0, 1);
                RgbColor rgbColor = hslColor.toRgb();
                writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                        mapValue(rgbColor.g, 0, 1),
                        mapValue(rgbColor.b, 0, 1),
                        1));
            }
        }
        return outputImage;
    }

    private static WritableImage lightByColor(Image inputImage, double lightness, int min, int max){
        int inputImageWidth = (int) inputImage.getWidth();
        int inputImageHeight = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();

        WritableImage outputImage = new WritableImage(inputImageWidth, inputImageHeight);
        PixelWriter writer = outputImage.getPixelWriter();

        if(min == 30 && max == 330){
            for (int y = 0; y < inputImageHeight; y++) {
                for (int x = 0; x < inputImageWidth; x++) {
                    var color = reader.getColor(x, y);
                    HslColor hslColor = new RgbColor(color.getRed(), color.getGreen(), color.getBlue()).toHsl();
                    if(hslColor.h > 330 || hslColor.h <= 30){
                        hslColor.l = mapValue(hslColor.l + lightness, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }else{
                        hslColor.l = mapValue(hslColor.l, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }
                }
            }
        }else{
            for (int y = 0; y < inputImageHeight; y++) {
                for (int x = 0; x < inputImageWidth; x++) {
                    var color = reader.getColor(x, y);
                    HslColor hslColor = new RgbColor(color.getRed(), color.getGreen(), color.getBlue()).toHsl();
                    if(hslColor.h > min && hslColor.h <= max){
                        hslColor.l = mapValue(hslColor.l + lightness, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }else{
                        hslColor.l = mapValue(hslColor.l, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }
                }
            }
        }

        return outputImage;
    }


    private static WritableImage drawBound(Image inputImage, WritableImage outputImage, double coefWidth, double coefHight){

        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = (int) (rectangle.getY() * coefHight); y < ((int) rectangle.getHeight() * coefHight + (int) rectangle.getY() * coefHight); y++) {
            for (int i = 0; i < 6; i++){
                writer.setColor((int) (rectangle.getX() * coefWidth) - i, y, Color.BLACK);
                writer.setColor((int) ((int) rectangle.getWidth() * coefHight + (int) rectangle.getX() * coefHight) + i, y, Color.BLACK);

            }
        }
        for (int x = (int) (rectangle.getX() * coefHight); x < ((int) rectangle.getWidth() * coefHight + (int) rectangle.getX() * coefHight); x++) {
            for (int i = 1; i < 6; i++){
                writer.setColor(x, (int) (rectangle.getY() * coefHight) - i, Color.BLACK);
                writer.setColor(x, (int) (((int) rectangle.getHeight() * coefHight + (int) rectangle.getY() * coefHight) + i), Color.BLACK);
            }

        }

        return outputImage;
    }

    private static WritableImage lightPartByColor(Image inputImage, double lightness, int min, int max){
        int inputImageWidth = (int) inputImage.getWidth();
        int inputImageHeight = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();

        WritableImage outputImage = new WritableImage(inputImageWidth, inputImageHeight);
        PixelWriter writer = outputImage.getPixelWriter();

        double coefWidth = Math.ceil((inputImageWidth / 180.0)) ;
        double coefHight = Math.ceil((inputImageHeight / 180.0)) ;

        outputImage = fillImage(inputImage, outputImage, 0);

        if(min == 30 && max == 330){
            for (int y = (int) (rectangle.getY() * coefHight); y < ((int) rectangle.getHeight() + (int) rectangle.getY()) * coefHight; y++) {
                for (int x = (int) (rectangle.getX() * coefWidth); x < ((int) rectangle.getWidth() + (int) rectangle.getX()) * coefWidth; x++) {
                    var color = reader.getColor(x, y);
                    HslColor hslColor = new RgbColor(color.getRed(), color.getGreen(), color.getBlue()).toHsl();
                    if(hslColor.h > 330 || hslColor.h <= 30){
                        hslColor.l = mapValue(hslColor.l + lightness, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }else{
                        hslColor.l = mapValue(hslColor.l, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }
                }
            }
        }else{
            for (int y = (int) (rectangle.getY() * coefHight); y < ((int) rectangle.getHeight() + (int) rectangle.getY()) * coefHight; y++) {
                for (int x = (int) (rectangle.getX() * coefWidth); x < ((int) rectangle.getWidth() + (int) rectangle.getX()) * coefWidth; x++) {
                    var color = reader.getColor(x, y);
                    HslColor hslColor = new RgbColor(color.getRed(), color.getGreen(), color.getBlue()).toHsl();
                    if(hslColor.h > min && hslColor.h <= max){
                        hslColor.l = mapValue(hslColor.l + lightness, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }else{
                        hslColor.l = mapValue(hslColor.l, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }
                }
            }
        }

        drawBound(inputImage, outputImage, coefWidth, coefHight);

        return outputImage;
    }

    public static Image reColor(Image inputImage, double lightness, Canvas canvas) {
        int inputImageWidth = (int) inputImage.getWidth();
        int inputImageHeight = (int) inputImage.getHeight();

        PixelReader reader = inputImage.getPixelReader();

        WritableImage outputImage = new WritableImage(inputImageWidth, inputImageHeight);
        PixelWriter writer = outputImage.getPixelWriter();

        double coefWidth = Math.ceil((inputImageWidth / 180.0)) ;
        double coefHight = Math.ceil((inputImageHeight / 180.0)) ;

        outputImage = fillImage(inputImage, outputImage, 0);

        if(rectangle.getHeight() == 0) {
            if(Objects.equals(colorFromComboBox, "червоним")){
                outputImage = lightByColor(inputImage, lightness, 30, 330);
            }
            else if(Objects.equals(colorFromComboBox, "жовтим")){
                outputImage = lightByColor(inputImage, lightness, 30, 90);
            }
            else if(Objects.equals(colorFromComboBox, "зеленим")){
                outputImage = lightByColor(inputImage, lightness, 90, 150);
            }
            else if(Objects.equals(colorFromComboBox, "блакитним")){
                outputImage = lightByColor(inputImage, lightness, 150, 210);
            }
            else if(Objects.equals(colorFromComboBox, "синім")){
                outputImage = lightByColor(inputImage, lightness, 210, 280);
            }
            else if(Objects.equals(colorFromComboBox, "пурпуровим")){
                outputImage = lightByColor(inputImage, lightness, 270, 330);
            }
            else{
                outputImage = fillImage(inputImage, outputImage, lightness);

            }

        }
        else{
            if(Objects.equals(colorFromComboBox, "червоним")){
                outputImage = lightPartByColor(inputImage, lightness, 30, 330);
            }
            else if(Objects.equals(colorFromComboBox, "жовтим")){
                outputImage = lightPartByColor(inputImage, lightness, 30, 90);
            }
            else if(Objects.equals(colorFromComboBox, "зеленим")){
                outputImage = lightPartByColor(inputImage, lightness, 90, 150);
            }
            else if(Objects.equals(colorFromComboBox, "блакитним")){
                outputImage = lightPartByColor(inputImage, lightness, 150, 210);
            }
            else if(Objects.equals(colorFromComboBox, "синім")){
                outputImage = lightPartByColor(inputImage, lightness, 210, 280);
            }
            else if(Objects.equals(colorFromComboBox, "пурпуровим")){
                outputImage = lightPartByColor(inputImage, lightness, 270, 330);
            }
            else{
                for (int y = (int) (rectangle.getY() * coefHight); y < ((int) rectangle.getHeight() + (int) rectangle.getY()) * coefHight; y++) {
                    for (int x = (int) (rectangle.getX() * coefWidth); x < ((int) rectangle.getWidth() + (int) rectangle.getX()) * coefWidth; x++) {
                        var color = reader.getColor(x, y);
                        HslColor hslColor = new RgbColor(color.getRed(), color.getGreen(), color.getBlue()).toHsl();
                        hslColor.l = mapValue(hslColor.l + lightness, 0, 1);
                        RgbColor rgbColor = hslColor.toRgb();
                        writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                                mapValue(rgbColor.g, 0, 1),
                                mapValue(rgbColor.b, 0, 1),
                                1));
                    }
                }
            }

            drawBound(inputImage, outputImage, coefWidth, coefHight);

        }

        return outputImage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lightnessSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            imageView.setImage(reColor(image, newValue.doubleValue() / 100, colorCanvas));
            lightnessText.setText(String.format("Яскравість: %d%%", newValue.intValue()));
        });

        final Light.Point anchor = new Light.Point();

        colorsComboBox.setOnAction(event -> {
            colorFromComboBox = (String) colorsComboBox.getValue();
            getcolorfromcombobox();
            System.out.println(colorFromComboBox);
        });

        imageView.setOnMousePressed(event -> {
            System.out.println(imageView.getFitWidth());
            anchor.setX(event.getX());
            anchor.setY(event.getY());
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
            rectangle.setHeight(0);
            rectangle.setWidth(0);
        });

        imageView.setOnMouseDragged(event -> {
            rectangle.setWidth(Math.abs(event.getX() - anchor.getX()));
            rectangle.setHeight(Math.abs(event.getY() - anchor.getY()));
            rectangle.setX(Math.min(anchor.getX(), event.getX()));
            rectangle.setY(Math.min(anchor.getY(), event.getY()));

            int inputImageWidth = (int) image.getWidth();
            int inputImageHeight = (int) image.getHeight();

            PixelReader reader = image.getPixelReader();

            WritableImage outputImage = new WritableImage(inputImageWidth, inputImageHeight);
            PixelWriter writer = outputImage.getPixelWriter();

            double coefWidth = Math.ceil((inputImageWidth / 180.0)) ;
            double coefHight = Math.ceil((inputImageHeight / 180.0)) ;
            //ініціалізуємо наш канвас справа
            for (int y = 0; y < inputImageHeight; y++) {
                for (int x = 0; x < inputImageWidth; x++) {
                    var color = reader.getColor(x, y);
                    HslColor hslColor = new RgbColor(color.getRed(), color.getGreen(), color.getBlue()).toHsl();
                    RgbColor rgbColor = hslColor.toRgb();

                    writer.setColor(x, y, new Color(mapValue(rgbColor.r, 0, 1),
                            mapValue(rgbColor.g, 0, 1),
                            mapValue(rgbColor.b, 0, 1),
                            1));
                }
            }
           //виділяємо прямокутник чорним
            for (int y = (int) (rectangle.getY() * coefHight) - 4; y < ((int) rectangle.getHeight() * coefHight + (int) rectangle.getY() * coefHight) + 4; y++) {
                for (int i = 0; i < 6; i++){
                    writer.setColor((int) (rectangle.getX() * coefWidth) - i, y, Color.BLACK);
                    writer.setColor((int) ((int) rectangle.getWidth() * coefHight + (int) rectangle.getX() * coefHight) + i, y, Color.BLACK);
                }
            }
            for (int x = (int) (rectangle.getX() * coefHight) - 2; x < ((int) rectangle.getWidth() * coefHight + (int) rectangle.getX() * coefHight) + 2; x++) {
                for (int i = 0; i < 6; i++){
                    writer.setColor(x, (int) (rectangle.getY() * coefHight) - i, Color.BLACK);
                    writer.setColor(x, (int) (((int) rectangle.getHeight() * coefHight + (int) rectangle.getY() * coefHight) + i), Color.BLACK);
                }
            }

            imageView.setImage(outputImage);

        });

        imageView.setOnMouseReleased(event -> {
            System.out.printf("X: %.2f, Y: %.2f, Width: %.2f, Height: %.2f%n",
                    rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        });

        //піпетка наша
        imageView.setOnMouseMoved(e -> {
            int x = (int) e.getX();
            int y = (int) e.getY();

            var rgbColor = imageView.snapshot(null, null).getPixelReader().getColor(x, y);

            colorCanvas.getGraphicsContext2D().setFill(rgbColor);
            colorCanvas.getGraphicsContext2D().fillRect(0, 0, colorCanvas.getHeight(), colorCanvas.getWidth());

            rgbLine.setText(new RgbColor(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()).toString());
            hslLine.setText(new RgbColor(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()).toHsl().toString());
        });
    }
    public void getcolorfromcombobox(){
        var rgbColor=new Color(0,0,0,0);

        if(Objects.equals(colorFromComboBox, "червоним")){
             rgbColor=new Color(1,0,0,1);
        }
        else if(Objects.equals(colorFromComboBox, "жовтим")){
            rgbColor=new Color(1,1,0,1);
        }
        else if(Objects.equals(colorFromComboBox, "зеленим")){
            rgbColor=new Color(0,1,0,1);
        }
        else if(Objects.equals(colorFromComboBox, "блакитним")){
            rgbColor=new Color(0,1,1,1);
        }
        else if(Objects.equals(colorFromComboBox, "синім")){
            rgbColor=new Color(0,0,1,1);
        }
        else if(Objects.equals(colorFromComboBox, "пурпуровим")){
            rgbColor=new Color(1,0,1,1);
        }else {
            rgbColor=new Color(1,1,1,1);
        }

        colorCanvas.getGraphicsContext2D().setFill(rgbColor);
        colorCanvas.getGraphicsContext2D().fillRect(0, 0, colorCanvas.getHeight(), colorCanvas.getWidth());

        rgbLine.setText(new RgbColor(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()).toString());
        hslLine.setText(new RgbColor(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()).toHsl().toString());
    }
}