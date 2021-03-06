package com.darkguardsman.railnet.ui.panels.curve;

import com.darkguardsman.railnet.api.rail.IRailPathPoint;
import com.darkguardsman.railnet.data.rail.segments.RailSegmentCurve;
import com.darkguardsman.railnet.lib.Pos;
import com.darkguardsman.railnet.ui.graphics.RenderPanel;
import com.darkguardsman.railnet.ui.graphics.rail.RailRenderUtil;
import com.darkguardsman.railnet.ui.panels.PanelAbstractTest;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

/**
 * Used to test logic for line rails visually
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/18.
 */
public class PanelCurveRailAlg extends PanelAbstractTest {

    protected JTextField startXField;
    protected JTextField startZField;
    protected JTextField endXField;
    protected JTextField endZField;
    protected JTextField startAngleField;
    protected JTextField endAngleField;

    public PanelCurveRailAlg() {
        super("Test path point generate for curved rails." +
                "\n\nMeant to act as a low level test without any snap point prediction." +
                "\nThis way path point generation can be tested without worrying about the state of snap logic.");
    }

    @Override
    protected void addRenderPanelRenders(RenderPanel panel) {
        super.addRenderPanelRenders(panel);
        panel.setViewBoundSize(20);
    }

    @Override
    protected JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 2));
        JButton button;

        panel.add(new JLabel("Start:"));
        panel.add(new JPanel());
        panel.add(new JLabel("X:"));
        panel.add(startXField = new JTextField("-5"));
        panel.add(new JLabel("Z:"));
        panel.add(startZField = new JTextField("-5"));

        addSpacer(panel);

        panel.add(new JLabel("End:"));
        panel.add(new JPanel());
        panel.add(new JLabel("X:"));
        panel.add(endXField = new JTextField("5"));
        panel.add(new JLabel("Z:"));
        panel.add(endZField = new JTextField("5"));

        addSpacer(panel);

        panel.add(new JLabel("Start Angle:"));
        panel.add(startAngleField = new JTextField("0"));

        panel.add(new JLabel("End Angle:"));
        panel.add(endAngleField = new JTextField("0"));

        addSpacer(panel);

        panel.add(new JPanel());
        button = new JButton("Generate");
        button.addActionListener((a) -> generateRail());
        panel.add(button);

        addSpacer(panel);
        addSpacer(panel);

        panel.add(new JPanel());
        button = new JButton("Clear");
        button.addActionListener((a) -> renderPanel.clear());
        panel.add(button);


        return panel;
    }

    protected void generateRail() {

        double endX;
        double endZ;
        try {
            endX = Double.parseDouble(endXField.getText().trim());
            endZ = Double.parseDouble(endZField.getText().trim());
        } catch (Exception e) {
            e.printStackTrace();
            //TODO display to user that data is invalid
            return;
        }
        generateRail(new Pos(endX, 0, endZ));
    }

    public void generateRail(Pos end) {
        double startX;
        double startZ;
        try {
            startX = Double.parseDouble(startXField.getText().trim());
            startZ = Double.parseDouble(startZField.getText().trim());
        } catch (Exception e) {
            e.printStackTrace();
            //TODO display to user that data is invalid
            return;
        }
        generateRail(new Pos(startX, 0, startZ), end);
    }

    protected void generateRail(Pos start, Pos end) {
        int startAngle;
        int endAngle;
        try {
            startAngle = Integer.parseInt(startAngleField.getText().trim());
            endAngle = Integer.parseInt(endAngleField.getText().trim());
        } catch (Exception e) {
            e.printStackTrace();
            //TODO display to user that data is invalid
            return;
        }
        generateRail(start, end, startAngle, endAngle);
    }

    public void generateRail(Pos start, Pos end, int startAngle, int endAngle) {
        clearTest();

        try {

            //Debug info so we can see the math
            log("Generating line rail for render");
            log("\tStart: " + start);
            log("\tend: " + end);
            log("\tAngles: " + startAngle);

            //Generate rail and get dots
            RailSegmentCurve segment = RailRenderUtil.generateCurveRail(pointRender, start, end, startAngle, endAngle);
            newRail(segment);

            //Data Debug
            int i = 0;
            for (IRailPathPoint dot : segment.getAllPaths().get(0).getPathPoints()) {
                log("\t\t[" + (i++) + "]: " + dot.x() + ", " + dot.y());
            }

            renderPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            renderPanel.clear();
            //TODO display error
        }

    }
}
