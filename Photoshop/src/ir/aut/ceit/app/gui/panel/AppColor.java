package ir.aut.ceit.app.gui.panel;

import javax.swing.*;
import java.awt.*;

public class AppColor extends JPanel {

    public JSlider jSlider;

    public AppColor() {
        jSlider = new JSlider(0, 255, 0);
        setPreferredSize(new Dimension(500, 100));
        jSlider.setMinorTickSpacing(5);
        jSlider.setMajorTickSpacing(10);
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);
        jSlider.setLabelTable(jSlider.createStandardLabels(50));
        jSlider.setPreferredSize(new Dimension(400 , 70));
        this.add(jSlider);
        this.setVisible(true);
    }
}
