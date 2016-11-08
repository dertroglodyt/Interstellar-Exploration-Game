/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hdc.commonlibrary.util;

import java.util.Random;

/**
 *
 * @author martin
 */
public final class Dice {

    public static double getDouble() {
        return rand.nextDouble();
    }

    public static int dice(int aDefault, double[][] v) {
        for (double[] v1: v) {
            if (getDouble() <= v1[1]) {
                return (int) v1[0];
            }
        }
        return aDefault;
    }

    public static double dice(double aDefault, double[][] v) {
        for (double[] v1: v) {
            if (getDouble() <= v1[1]) {
                return v1[0];
            }
        }
        return aDefault;
    }

    public static double[][] transform(double[][] v) {
        double s = 0.0;
        // calc sum of chances for normalization
        for (double[] v1: v) {
            s += v1[1];
        }
        double last = 0;
        for (double[] v1: v) {
            last += v1[1] / s;
            v1[1] = last;
        }
        return v;
    }

    public static double dice(double[][] v) {
        // throw dice for normalized chances
        double d = getDouble();
        for (double[] v1: v) {
            if (d < v1[1]) {
                return (int) v1[0];
            }
        }
        return v[0][0];
    }

    private static final Random rand = new Random();

    private Dice() {
    }

}
