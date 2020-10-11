package ir.aut.ceit.app.gui.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AppMainPanel extends JPanel {

    private BufferedImage img;
    private int rotationDegree;
    private int xPressed;
    private int yPressed;
    private int xDest;
    private int yDest;

    public AppMainPanel(BufferedImage bufferedImage) {
        this.img = bufferedImage;
        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        this.setLayout(null);
        xPressed = 0;
        yPressed = 0;
        xDest = img.getWidth();
        yDest = img.getHeight();
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        int x = this.getWidth() / 2;
        int y = this.getHeight() / 2;
        graphics2D.rotate(Math.toRadians(getRotationDegree()), x, y);
        super.paintComponent(graphics);
        if (xDest - xPressed < img.getWidth() && yDest - yPressed < img.getHeight()) {
            img = img.getSubimage(xPressed, yPressed, xDest - xPressed, yDest - yPressed);
        }
        graphics.drawImage(img, 0, 0, this);
    }

    private int getRotationDegree() {
        return rotationDegree;
    }

    public void setRotationDegree(int rotationDegree) {
        this.rotationDegree = rotationDegree;
    }

    public void setxPressed(int xPressed) {
        this.xPressed = xPressed;
    }

    public void setyPressed(int yPressed) {
        this.yPressed = yPressed;
    }

    public void setxDest(int xDest) {
        this.xDest = xDest;
    }

    public void setyDest(int yDest) {
        this.yDest = yDest;
    }

    public BufferedImage getImg() {
        return img;
    }
}
