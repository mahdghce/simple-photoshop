package ir.aut.ceit.app.gui.panel;

import javax.swing.*;
import java.awt.*;

public class AppRotatePanel extends JPanel {

    public JSlider jSlider;

    public AppRotatePanel() {
        jSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        setPreferredSize(new Dimension(500 , 100));
        jSlider.setMinorTickSpacing(4);
        jSlider.setMajorTickSpacing(10);
        this.setBorder(BorderFactory.createTitledBorder("Rotate Slider"));
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);
        jSlider.setLabelTable(jSlider.createStandardLabels(45));
        jSlider.setPreferredSize(new Dimension(400 , 70));
        this.add(jSlider);
        this.setVisible(true);
    }
}
