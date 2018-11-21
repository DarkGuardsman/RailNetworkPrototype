package com.darkguardsman.railnet.lib;

/**
 * series of helper methods
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/20/18.
 */
public final class MathHelpers {

    /**
     * Wraps the angle between 0 - 360
     *
     * @param angle - angle in degrees
     * @return angle between 0 - 360;
     */
    public static double wrapTo360(double angle) {

        //Works the same as %
        int div = (int) angle / 360;
        if (angle > 0) {
            angle -= div * 360;
        } else if (angle < 0) {
            angle += div * 360;
        }

        //Less then zero, wrap back around
        if (angle < 0) {
            return angle + 360;
        }

        return angle;
    }

    /**
     * Wraps the angle between 0 - 360
     *
     * @param angle - angle in degrees
     * @return angle between 0 - 360;
     */
    public static int wrapTo360(int angle) {
        //Reduce
        angle = angle % 360;

        //Less then zero, wrap back around
        if (angle < 0) {
            return angle + 360;
        }

        return angle;
    }

    /**
     * Position on the X axis for the given angle
     *
     * @param radius - radius of the circle
     *               * @param angle - angle, in degrees
     *               * @return position
     */
    public static double getXPositionOnCircle(double radius, double angle) {
        return radius * Math.cos(Math.toRadians(angle));
    }

    /**
     * Position on the Y axis for the given angle
     *
     * @param radius - radius of the circle
     * @param angle  - angle, in degrees
     * @return position
     */
    public static double getYPositionOnCircle(double radius, double angle) {
        return radius * Math.sin(Math.toRadians(angle));
    }


    /**
     * Get distance between angles. Order of angles does
     * not matter as it will find the shortest distance
     * in either direction around a circle.
     *
     * @param a - first angle, degrees
     * @param b - second angle, degrees
     * @return angle delta in degrees
     */
    public static int getAngleDelta(int a, int b) {
        return Math.min((a - b + 360) % 360, (b - a + 360) % 360);
    }
}
