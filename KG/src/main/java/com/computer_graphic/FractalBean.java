package com.computer_graphic;

import javafx.scene.paint.Color;

public class FractalBean {

    private double reMin;
    private double reMax;
    private double imMin;
    private double imMax;

    private double z;
    private double zi;

    private int convergenceSteps;
    private Color convergenceColor = Color.BLACK;
    private Color colorSchema = Color.GREEN;

    public FractalBean(int convergenceSteps, double reMin, double reMax, double imMin, double imMax, double z, double zi) {
        this.convergenceSteps = convergenceSteps;
        this.reMin = reMin;
        this.reMax = reMax;
        this.imMin = imMin;
        this.imMax = imMax;
        this.z = z;
        this.zi = zi;
    }

    public void moveRight(double factor) {
        double offset = (reMax - reMin) * factor;
        reMax += offset;
        reMin += offset;
    }

    public void moveUp(double factor) {
        double offset = (imMax - imMin) * factor;
        imMax += offset;
        imMin += offset;
    }

    public void zoom(double factor) {
        double xmc = (reMax + reMin) / 2;
        double ymc = (imMax + imMin) / 2;
        reMin *= factor;
        reMax *= factor;
        imMin *= factor;
        imMax *= factor;
        double xdiff = xmc - (reMax + reMin) / 2;
        double ydiff = ymc - (imMax + imMin) / 2;
        reMin += xdiff;
        reMax += xdiff;
        imMin += ydiff;
        imMax += ydiff;
    }

    public boolean isIsMandelbrot() {
        return z * zi == 0;
    }

    public static int checkConvergence(double ci, double c, double z, double zi, int convergenceSteps) {
        for (int i = 0; i < convergenceSteps; i++) {
            double ziT = 2 * (z * zi);
            double zT = z * z - (zi * zi);
            z = zT + c;
            zi = ziT + ci;

            if (z * z + zi * zi >= 4.0) {
                return i;
            }
        }
        return convergenceSteps;
    }

    public void setReMin(double reMin) {
        this.reMin = reMin;
    }

    public void setReMax(double reMax) {
        this.reMax = reMax;
    }

    public void setImMin(double imMin) {
        this.imMin = imMin;
    }

    public void setImMax(double imMax) {
        this.imMax = imMax;
    }

    public int getConvergenceSteps() {
        return convergenceSteps;
    }

    public double getReMin() {
        return reMin;
    }

    public double getReMax() {
        return reMax;
    }

    public double getImMin() {
        return imMin;
    }

    public double getImMax() {
        return imMax;
    }

    public double getZ() {
        return z;
    }

    public double getZi() {
        return zi;
    }

    public void setConvergenceSteps(int convergenceSteps) {
        this.convergenceSteps = convergenceSteps;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setZi(double zi) {
        this.zi = zi;
    }

    public Color getConvergenceColor() {
        return convergenceColor;
    }

    public void setConvergenceColor(Color convergenceColor) {
        this.convergenceColor = convergenceColor;
    }

    public Color getColorSchema() {
        return colorSchema;
    }

    public void setColorSchema(Color color) {
        this.colorSchema = color;
    }

    public Color getColorSchema(double t) {
        return Color.hsb(colorSchema.getHue(), colorSchema.getSaturation(), colorSchema.getBrightness() * t);
    }
}
