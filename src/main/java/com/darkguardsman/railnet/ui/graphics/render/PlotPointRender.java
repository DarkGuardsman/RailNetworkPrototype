package com.darkguardsman.railnet.ui.graphics.render;

import com.darkguardsman.railnet.ui.graphics.data.PlotConnection;
import com.darkguardsman.railnet.ui.graphics.data.PlotPoint;
import com.darkguardsman.railnet.ui.graphics.RenderPanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/18.
 */
public class PlotPointRender implements IPlotRenderObject {

    /**
     * Data to display in the panel
     */
    protected final List<PlotPoint> data = new ArrayList();
    protected final List<PlotConnection> lines = new ArrayList();

    protected final Consumer<List<PlotPoint>> dataRefreshFunction;

    public PlotPointRender(Consumer<List<PlotPoint>> dataRefreshFunction) {
        this.dataRefreshFunction = dataRefreshFunction;
    }

    @Override
    public void draw(Graphics2D g2, RenderPanel renderPanel) {
        data.forEach(pos -> renderPanel.drawCircle(g2, pos.color, pos.x, pos.y, pos.size, true));
        lines.forEach(line -> renderPanel.drawLine(g2, line));
    }

    public void add(PlotPoint plotPoint) {
        data.add(plotPoint);
    }

    public void addPlusLinkLast(PlotPoint plotPoint, Color lineColor) {
        data.add(plotPoint);
        if (lineColor != null && data.size() > 0) {
            lines.add(new PlotConnection(data.get(0), plotPoint, lineColor));
        }
    }

    @Override
    public double getMaxY() {
        if (data.size() == 0) {
            return 0;
        }
        return data.stream().max(Comparator.comparingDouble(PlotPoint::y)).get().y;
    }

    @Override
    public double getMaxX() {
        if (data.size() == 0) {
            return 0;
        }
        return data.stream().max(Comparator.comparingDouble(PlotPoint::x)).get().x;
    }

    @Override
    public double getMinY() {
        if (data.size() == 0) {
            return 0;
        }
        return data.stream().min(Comparator.comparingDouble(PlotPoint::y)).get().y;
    }

    @Override
    public double getMinX() {
        if (data.size() == 0) {
            return 0;
        }
        return data.stream().min(Comparator.comparingDouble(PlotPoint::x)).get().x;
    }

    @Override
    public void clearData() {
        data.clear();
        lines.clear();
    }

    @Override
    public void refreshData() {
        if (dataRefreshFunction != null) {
            clearData();
            dataRefreshFunction.accept(data);
        }
    }
}
