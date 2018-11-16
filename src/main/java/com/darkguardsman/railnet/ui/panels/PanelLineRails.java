package com.darkguardsman.railnet.ui.panels;

import com.darkguardsman.railnet.api.RailHeading;
import com.darkguardsman.railnet.api.rail.IRailPathPoint;
import com.darkguardsman.railnet.data.rail.segments.RailSegmentLine;
import com.darkguardsman.railnet.lib.RailUtils;
import com.darkguardsman.railnet.ui.graphics.data.PlotPoint;
import com.darkguardsman.railnet.ui.graphics.RenderPanel;
import com.darkguardsman.railnet.ui.graphics.rail.RailRenderUtil;
import com.darkguardsman.railnet.ui.graphics.render.PlotCenterRender;
import com.darkguardsman.railnet.ui.graphics.render.PlotGridRender;
import com.darkguardsman.railnet.ui.graphics.render.PlotPointRender;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to test logic for line rails visually
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/18.
 */
public class PanelLineRails extends JPanel {

    protected RenderPanel renderPanel;
    protected PlotPointRender pointRender;

    protected JTextField distanceField;

    public PanelLineRails() {
        setLayout(new BorderLayout());
        add(createRenderPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.WEST);
    }

    private JPanel createRenderPanel() {
        renderPanel = new RenderPanel();
        renderPanel.upperBound = new Dimension(10, 10);
        renderPanel.lowerBound = new Dimension(-10, -10);

        renderPanel.addRendersToRun(new PlotGridRender(1, 1));
        renderPanel.addRendersToRun(new PlotGridRender(2, 2, Color.BLUE));
        renderPanel.addRendersToRun(new PlotCenterRender());
        renderPanel.addRendersToRun(pointRender = new PlotPointRender(null));
        return renderPanel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 2));
        JButton button;

        panel.add(new JLabel("Distance:"));
        panel.add(distanceField = new JTextField("10"));

        for (RailHeading railHeading : RailHeading.values()) {
            button = new JButton("" + railHeading.name());
            button.addActionListener((a) -> generateLineRail(railHeading));
            panel.add(button);
        }

        //Spacer
        panel.add(new JPanel());
        panel.add(new JPanel());

        //Spacer
        panel.add(new JPanel());
        panel.add(new JPanel());

        //Spacer
        panel.add(new JPanel());
        panel.add(new JPanel());

        panel.add(new JPanel());
        button = new JButton("Clear");
        button.addActionListener((a) -> renderPanel.clear());
        panel.add(button);


        return panel;
    }

    protected void generateLineRail(RailHeading heading) {
        double distance = Math.floor(Double.parseDouble(distanceField.getText().trim()));
        generateLineRail(heading, distance);
    }

    protected void generateLineRail(RailHeading heading, double distance) {

        //Reset data
        renderPanel.clear();

        //Set start position negative of center for best visual layout
        double x = -heading.offsetX * (distance / 2);
        double z = -heading.offsetZ * (distance / 2);

        //Debug info so we can see the math
        System.out.println("Generating line rail for render");
        System.out.println("\tWith Heading: " + heading);
        System.out.println("\tStart: " + x + ", " + z);
        System.out.println("\tDistance: " + distance);

        //Generate rail and get dots, dots include two reference dots to show start and end points more clearly
        List<PlotPoint> dots = new ArrayList();
        RailRenderUtil.generateLineRail(dots, heading, x, z, distance);

        //More debug
        System.out.println("\tPoints:");
        System.out.println("\t\tSize: " + (dots.size() - 2)); //-2 is to remove the reference points

        //Extra line to visual show start to end path, added to debug for issues
        pointRender.addLine(dots.get(0), dots.get(dots.size() - 1), Color.blue, 8);


        //Add dots to render, include lines to trace path easier
        for (int i = 0; i < dots.size(); i++) {

            PlotPoint dot = dots.get(i);

            //Debug data to show the exact data used
            System.out.println("\t\t[" + (i - 2) + "]: " + dot.x + ", " + dot.y);

            //Adds node and sets a line to last node
            pointRender.addPlusLinkLast(dot, Color.CYAN, 2); //TODO consider moving links to data generator
        }

        //Trigger UI to draw new data
        renderPanel.repaint();
    }
}
