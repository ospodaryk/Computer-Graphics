package com.computer_graphic.model.colors;

public class HslColor {

    public double h;
    public double s;
    public double l;

    public HslColor(double h, double s, double l) {
        this.h = h;
        this.s = s;
        this.l = l;
    }


    @Override
    public String toString() {
        return String.format("(%d°, %d%%, %d%%)", (int) (h), (int) (s * 100), (int) (l * 100));
    }

    public RgbColor toRgb() {
        double h = this.h % 360.0f;

        h /= 360f;

        double q = 0;

        if (l < 0.5)
            q = l * (1 + s);
        else
            q = (l + s) - (s * l);

        double p = 2 * l - q;

        double r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
        double g = Math.max(0, HueToRGB(p, q, h));
        double b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

        return new RgbColor(r, g, b);
    }

    private static double HueToRGB(double p, double q, double h) {
        if (h < 0)
            h += 1;

        if (h > 1)
            h -= 1;

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }

    public static void main(String[] args) {
    }
}
