package com.darkguardsman.railnet.ui.graphics;


import com.darkguardsman.railnet.ui.graphics.render.IPlotRenderObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Simple panel used to draw objects to screen
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert)
 */
public class RenderPanel extends JPanel {
    protected List<IPlotRenderObject> rendersToRun = new ArrayList();
    /**
     * Spacing from each side
     */
    public int PAD = 20;

    public Dimension lowerBound;
    public Dimension upperBound;

    public RenderPanel() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBorder(g2);
        rendersToRun.forEach(render -> render.draw(g2, this));
    }

    /**
     * Draws a border around the component to define the edge
     *
     * @param g2
     */
    protected void drawBorder(Graphics2D g2) {
        g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2); //TODO why -2?
    }

    public void drawCircle(Graphics2D g2, Color color, double point_x, double point_y, double size, boolean fill) {
        drawEllipse(g2, color, point_x, point_y, size, size, fill);
    }

    public void drawEllipse(Graphics2D g2, Color color, double point_x, double point_y, double size_x, double size_y, boolean fill) {
        //Calculate scale to fit display
        double scaleX = getScaleX();
        double scaleY = getScaleY();

        //Get x & y, render position is based on data point plus offset, scaled to match view, and then offset by padding to avoid edges
        double x = scaleX * (point_x + getOffsetX());
        double y = scaleY * (point_y + getOffsetY());

        //Only render if the ellipse will be in view TODO check size not just center
        if (x >= 0 && x <= getWidth() && y <= getHeight()) {
            //Generate circle
            Ellipse2D circle = new Ellipse2D.Double(x - (size_x / 2), y - (size_y / 2), size_x, size_y);

            //Set color
            g2.setPaint(color != null ? color : Color.red);

            //Draw
            if (fill) {
                g2.fill(circle);
            } else {
                g2.draw(circle);
            }
        }
    }

    public void drawLine(Graphics2D g2, Color color, double point_x, double point_y, double point_x2, double point_y2) {
        //Calculate scale to fit display
        double scaleX = getScaleX();
        double scaleY = getScaleY();

        double x1 = (point_x + getOffsetX()) * scaleX;
        double y1 = (point_y + getOffsetY()) * scaleY;

        double x2 = (point_x2 + getOffsetX()) * scaleX;
        double y2 = (point_y2 + getOffsetY()) * scaleY;

        //TODO trim line to fit inside view & padding

        g2.setColor(color);
        g2.drawLine((int) Math.floor(x1), (int) Math.floor(y1), (int) Math.ceil(x2), (int) Math.ceil(y2));
    }

    public void drawVerticalLine(Graphics2D g2, Color color, double x) {
        double scaleX = getScaleX();
        int x1 = (int) Math.floor((x + getOffsetX()) * scaleX);

        g2.setColor(color);
        g2.drawLine(x1, PAD, x1, getHeight() - PAD);
    }

    public void drawHorizontalLine(Graphics2D g2, Color color, double y) {
        double scaleY = getScaleY();
        int y1 = (int) Math.floor((y + getOffsetY()) * scaleY);

        g2.setColor(color);
        g2.drawLine(PAD, y1, getWidth() - PAD, y1);
    }

    public void drawBox(Graphics2D g2, Color color, double x, double y, double size_x, double size_y, boolean filled) {
        double scaleX = getScaleX();
        double scaleY = getScaleY();

        double x1 = (x + getOffsetX()) * scaleX;
        double y1 = (y + getOffsetY()) * scaleY;

        double x2 = size_x * scaleX;
        double y2 = size_y * scaleY;

        g2.setColor(color);
        if (filled) {
            g2.fillRect((int) x1, (int) y1, (int) x2, (int) y2);
        } else {
            g2.drawRect((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }

    /**
     * Scale to draw the data on the screen.
     * <p>
     * Modifies the position to correspond to the pixel location
     *
     * @return scale of view ((width - padding) / size)
     */
    public double getScaleX() {
        if (upperBound != null && lowerBound != null) {
            return (getWidth() - 2 * PAD) / (upperBound.width - lowerBound.width);
        }
        return (double) (getWidth() - 2 * PAD) / getRenderComponentWidth();
    }

    /**
     * Scale to draw the data on the screen.
     * <p>
     * Modifies the position to correspond to the pixel location
     *
     * @return scale of view ((width - padding) / size)
     */
    public double getScaleY() {
        if (upperBound != null && lowerBound != null) {
            return (getWidth() - 2 * PAD) / (upperBound.height - lowerBound.height);
        }
        return (double) (getHeight() - 2 * PAD) / getRenderComponentHeight();
    }

    public double getDrawMaxX() {
        return upperBound != null ? upperBound.width : getPointMaxX();
    }

    public double getDrawMaxY() {
        return upperBound != null ? upperBound.height : getPointMinY();
    }

    public double getDrawMinX() {
        return lowerBound != null ? lowerBound.width : getPointMaxX();
    }

    public double getDrawMinY() {
        return lowerBound != null ? lowerBound.height : getPointMinY();
    }

    /**
     * Offset for the data to ensure
     * all data renders positive x & y
     *
     * @return
     */
    public double getOffsetX() {
        if (lowerBound != null) {
            return -lowerBound.width;
        }

        return -getPointMinX();
    }

    /**
     * Offset for the data to ensure
     * all data renders positive x & y
     *
     * @return
     */
    public double getOffsetY() {
        if (lowerBound != null) {
            return -lowerBound.height;
        }
        return -getPointMinY();
    }

    /**
     * The distance components span in the view
     * in the X axis. This is not scaled to screen
     * size but to data size.
     *
     * @return
     */
    public double getRenderComponentWidth() {
        return getPointMaxX() - getPointMinX();
    }

    /**
     * The distance components span in the view
     * in the Y axis. This is not scaled to screen
     * size but to data size;
     *
     * @return
     */
    public double getRenderComponentHeight() {
        return getPointMaxY() - getPointMaxY();
    }

    /**
     * Max y value in the data set
     *
     * @return
     */
    public double getPointMaxY() {
        return rendersToRun.stream()
                .filter(a -> a.hasSize())
                .max(Comparator.comparingDouble(IPlotRenderObject::getMaxY))
                .orElseGet(() -> new IPlotRenderObject() {
                    @Override
                    public void draw(Graphics2D g2, RenderPanel renderPanel) {

                    }

                    @Override
                    public double getMaxY() {
                        return 0;
                    }
                }).getMaxY();
    }

    /**
     * Max x value in the data set
     *
     * @return
     */
    public double getPointMaxX() {
        return rendersToRun.stream()
                .filter(a -> a.hasSize())
                .max(Comparator.comparingDouble(IPlotRenderObject::getMaxX))
                .orElseGet(() -> new IPlotRenderObject() {
                    @Override
                    public void draw(Graphics2D g2, RenderPanel renderPanel) {

                    }

                    @Override
                    public double getMaxX() {
                        return 0;
                    }
                }).getMaxX();
    }

    /**
     * Max y value in the data set
     *
     * @return
     */
    public double getPointMinY() {
        return rendersToRun.stream()
                .filter(a -> a.hasSize())
                .min(Comparator.comparingDouble(IPlotRenderObject::getMinY))
                .orElseGet(() -> new IPlotRenderObject() {
                    @Override
                    public void draw(Graphics2D g2, RenderPanel renderPanel) {

                    }

                    @Override
                    public double getMinY() {
                        return 0;
                    }
                }).getMinY();
    }

    /**
     * Max x value in the data set
     *
     * @return
     */
    public double getPointMinX() {
        return rendersToRun.stream()
                .filter(a -> a.hasSize())
                .min(Comparator.comparingDouble(IPlotRenderObject::getMinX))
                .orElseGet(() -> new IPlotRenderObject() {
                    @Override
                    public void draw(Graphics2D g2, RenderPanel renderPanel) {

                    }

                    @Override
                    public double getMinX() {
                        return 0;
                    }
                }).getMinX();
    }

    public void addRendersToRun(IPlotRenderObject renderFunction) {
        rendersToRun.add(renderFunction);
    }

    public void clear() {
        rendersToRun.forEach(r -> r.clearData());
    }
}