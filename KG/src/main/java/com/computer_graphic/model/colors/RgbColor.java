package com.computer_graphic.model.colors;

public class RgbColor {

    public double r;
    public double g;
    public double b;

    public RgbColor(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public HslColor toHsl() {
        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        double delta = max - min;

        double h_;
        if (delta == 0) {
            h_ = 0;
        } else if (max == r) {
            h_ = (g - b) / delta;
            if (h_ < 0) h_ += 6.f;
        } else if (max == g) {
            h_ = (b - r) / delta + 2.f;
        } else {
            h_ = (r - g) / delta + 4.f;
        }
        double h = 60.f * h_;

        double l = (max + min) * 0.5f;

        double s;
        if (delta == 0) {
            s = 0.f;
        } else {
            s = delta / (1 - Math.abs(2.f * l - 1.f));
        }

        return new HslColor(h, s, l);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", (int) (r * 256), (int) (g * 256), (int) (b * 256));
    }
}
