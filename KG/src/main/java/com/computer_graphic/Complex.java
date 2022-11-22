package com.computer_graphic;

public class Complex {

    private double re, im;

    public Complex(){
        this.re = 0;
        this.im = 0;
    }

    public Complex(double re, double im){
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return this.re;
    }

    public double getIm() {
        return this.im;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public void setIm(double im) {
        this.im = im;
    }

    public double arg(){
        return arg(this);
    }

    public Complex add(double n){
        return new Complex( n + re, im);
    }

    public Complex multiply(double n){
        return new Complex( n * re, n * im);
    }

    public Complex multiply(Complex z2) {
        return new Complex(this.re * z2.im - this.im * z2.im, this.re * z2.im + this.im * z2.re);
    }

    public double abs(){
        return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
    }

    public Complex divide(double n){
        return this.multiply(1.0 / n);
    }

    public Complex pow(double n){
        return pow(this, n);
    }

    public Complex add(Complex z){
        return add(this, z);
    }

    public Complex subtract(Complex z){
        return subtract(this, z);
    }

    /*public double divide(Complex z){
        return divide(this, z);
    }*/

    public static Complex add(Complex z1, Complex z2){
        return new Complex( z1.re + z2.re,
                z1.im + z2.im);
    }

    public static Complex subtract(Complex z1, Complex z2){
        return new Complex(z1.re - z2.re, z1.im - z2.im);
    }

    public static Complex divide(Complex z1, Complex z2){
        double k = Math.pow(z2.im, 2) + Math.pow(z2.re, 2);
        return new Complex( (z1.re * z2.re + z1.im * z2.im) / k,
                (z1.im * z2.re - z1.re * z2.im) / k);
    }

    public static double abs(Complex z){
        return Math.sqrt(Math.pow(z.re, 2) + Math.pow(z.im, 2));
    }

    public static double arg(Complex z){
        return Math.atan2(z.im, z.re);
    }

    public static Complex pow(Complex z1, double z2){
        double r = Math.pow(z1.abs(), z2);
        double theta = z2 * z1.arg();
        return new Complex( Math.cos(theta) * r, Math.sin(theta) * r);
    }

    public static boolean equals(Complex z1, Complex z2, double tolerance){
        return (euclideanDistance(z1, z2) <= tolerance);
    }


    public boolean equals(Complex z, double tolerance){
        return equals(this, z, tolerance);
    }


    public double euclideanDistance(Complex z){
        return euclideanDistance(this, z);
    }

    public static double euclideanDistance(Complex z1, Complex z2){
        return Math.sqrt(Math.pow(z1.re - z2.re, 2) + Math.pow(z1.im - z2.im, 2));
    }

    @Override
    public String toString() {
        return "(" + re + "," + im + ")";
    }

}

