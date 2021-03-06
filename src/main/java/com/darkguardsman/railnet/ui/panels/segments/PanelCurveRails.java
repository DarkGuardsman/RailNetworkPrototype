package com.darkguardsman.railnet.ui.panels.segments;

import com.darkguardsman.railnet.data.rail.segments.RailSegmentCurve;
import com.darkguardsman.railnet.lib.Pos;
import com.darkguardsman.railnet.ui.graphics.RenderPanel;
import com.darkguardsman.railnet.ui.graphics.data.PlotPoint;
import com.darkguardsman.railnet.ui.graphics.rail.RailRenderUtil;
import com.darkguardsman.railnet.ui.graphics.render.PlotCenterRender;
import com.darkguardsman.railnet.ui.graphics.render.PlotGridRender;
import com.darkguardsman.railnet.ui.graphics.render.PlotPointRender;
import com.darkguardsman.railnet.ui.panels.PanelAbstractTest;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to test logic for line rails visually
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/18.
 */
public class PanelCurveRails extends PanelAbstractTest {

    protected JTextField scaleField;

    public PanelCurveRails() {
        super("Test common shapes for curved rails");
    }

    @Override
    protected void addRenderPanelRenders(RenderPanel panel)
    {
        super.addRenderPanelRenders(panel);
        panel.setViewBoundSize(5);
    }

    @Override
    protected JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 2));
        JButton button;

        panel.add(new JLabel("Scale: "));
        panel.add(scaleField = new JTextField("1"));

        for (RailTestSet railTestSet : RailTestSet.values()) {
            button = new JButton(railTestSet.name().replace("_", " "));
            button.addActionListener((a) -> generate(railTestSet));
            panel.add(button);
        }

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

    protected void generate(RailTestSet railTestSet) {
        clearTest();

        int scale;

        try {
            scale = (int) Double.parseDouble(scaleField.getText().trim());
            scaleField.setText("" + scale);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            //TODO show error
            return;
        }

        int size = scale * 2 + 2; //TODO make method to set grid size
        renderPanel.upperBound = new Dimension(size, size);
        renderPanel.lowerBound = new Dimension(-size, -size);

        /* RailRenderUtil.generateLineRail(pointRender,
                new Pos(0, 0, 0),
                new Pos(railTestSet.endX, 0, railTestSet.endZ),
                railTestSet.startAngle, railTestSet.endAngle, true);*/

        renderPanel.repaint();
    }

    enum RailTestSet {
        UP_LEFT(0, 270, 2, 2), //TODO use heading enum to map enter & exit angles and test points
        UP_RIGHT(0, 90, -2, 2), //TODO Ex: SOUTH(180) to EAST(90) & START(0, 0) to END (2, 0)
        DOWN_LEFT(180, 90, -2, -2),
        DOWN_RIGHT(180, 270, 2, -2);

        public final int startAngle;
        public final int endAngle;
        public final int endX;
        public final int endZ;

        RailTestSet(int startAngle, int endAngle, int endX, int endZ) {
            this.startAngle = startAngle;
            this.endAngle = endAngle;
            this.endX = endX;
            this.endZ = endZ;
        }
    }
}
