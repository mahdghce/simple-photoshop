package ir.aut.ceit.app.gui.frame;

import com.jhlabs.image.*;
import com.jhlabs.image.GrayFilter;
import ir.aut.ceit.app.gui.panel.AppColor;
import ir.aut.ceit.app.gui.panel.AppMainPanel;
import ir.aut.ceit.app.gui.panel.AppRotatePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AppFrame extends JFrame {
    private AppMainPanel appMainPanel;
    private BufferedImage mainImage;
    private AppRotatePanel appRotatePanel;
    private AppColor appRedSlider;
    private AppColor appBlueSlider;
    private AppColor appGreenSlider;
    private JTextField jTextField;
    private JButton btm;
    private Point initialLoc;
    private Point initialLocOnScreen;
    private int xPressed;
    private int yPressed;
    private int xDest;
    private int yDest;

    public AppFrame(String name, int num, int width, int height) {
        super(name);
        this.setLayout(new FlowLayout());
        this.setSize(width, height);
        this.setDefaultCloseOperation(num);
        JMenuBar jMenuBar = new JMenuBar();

        JMenu jMenu1 = new JMenu("File");
        JMenu jMenu2 = new JMenu("Edit");

        JMenuItem jMenuItem1 = new JMenuItem("New");
        JMenuItem jMenuItem2 = new JMenuItem("Open");
        JMenuItem jMenuItem3 = new JMenuItem("Close");
        JMenuItem jMenuItem4 = new JMenuItem("Save");

        jMenu1.add(jMenuItem1);
        jMenu1.add(jMenuItem2);
        jMenu1.add(jMenuItem3);
        jMenu1.add(jMenuItem4);

        JMenuItem jMenuItem6 = new JMenuItem("Add text");
        JMenuItem jMenuItem7 = new JMenuItem("Rotate slider");
        JMenuItem jMenuItem8 = new JMenuItem("Color");
        JMenuItem jMenuItem9 = new JMenuItem("Crop");
        JMenuItem jMenuItem10 = new JMenuItem("Filters");
        JMenuItem jMenuItem11 = new JMenuItem("Stickers");

        jMenu2.add(jMenuItem6);
        jMenu2.add(jMenuItem7);
        jMenu2.add(jMenuItem8);
        jMenu2.add(jMenuItem9);
        jMenu2.add(jMenuItem10);
        jMenu2.add(jMenuItem11);

        jMenuBar.add(jMenu1);
        jMenuBar.add(jMenu2);

        jMenuItem1.addActionListener(new newItemListener());
        jMenuItem2.addActionListener(new openItemListener());
        jMenuItem3.addActionListener(new closeItemListener());
        jMenuItem4.addActionListener(new saveItemLister());
        jMenuItem6.addActionListener(new addTextItemListener());
        jMenuItem7.addActionListener(new rotateItemListener());
        jMenuItem8.addActionListener(new colorItemListener());
        jMenuItem9.addActionListener(new cropItemListener());
        jMenuItem10.addActionListener(new filtersItemListener());
        jMenuItem11.addActionListener(new addStickerItemListener());

        this.setJMenuBar(jMenuBar);
        this.setVisible(true);
    }

    private class newItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mainImage == null) {
                mainImage = new BufferedImage(800, 1200, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = mainImage.getGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, 800, 1200);
                appMainPanel = new AppMainPanel(mainImage);
                AppFrame.this.add(appMainPanel);
                AppFrame.this.setVisible(true);
            }
        }
    }

    private class openItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mainImage == null) {
                JFileChooser jFileChooser = new JFileChooser();
                int returnValue = jFileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    try {
                        mainImage = ImageIO.read(file);
                        appMainPanel = new AppMainPanel(mainImage);
                        AppFrame.this.add(appMainPanel);
                        AppFrame.this.setVisible(true);
                        AppFrame.this.revalidate();
                        AppFrame.this.repaint();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class closeItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mainImage != null) {
                AppFrame.this.appMainPanel.setVisible(false);
                mainImage = null;
                if (appRotatePanel != null) {
                    AppFrame.this.appRotatePanel.setVisible(false);
                    appRotatePanel = null;
                }
                if (appGreenSlider != null && appRedSlider != null && appBlueSlider != null) {
                    AppFrame.this.appRedSlider.setVisible(false);
                    AppFrame.this.appBlueSlider.setVisible(false);
                    AppFrame.this.appGreenSlider.setVisible(false);
                    appRedSlider = null;
                    appBlueSlider = null;
                    appGreenSlider = null;
                }
                if (jTextField != null) {
                    AppFrame.this.jTextField.setVisible(false);
                    jTextField = null;
                }
                if (btm != null) {
                    AppFrame.this.btm.setVisible(false);
                    btm = null;
                }
            }
        }
    }

    private class saveItemLister implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mainImage != null) {
                JFileChooser jFileChooser = new JFileChooser();
                FileNameExtensionFilter pFilter = new FileNameExtensionFilter("png", "png");
                jFileChooser.setFileFilter(pFilter);
                int status = jFileChooser.showSaveDialog(AppFrame.this);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    String fileName;
                    try {
                        fileName = selectedFile.getCanonicalPath();
                        selectedFile = new File(fileName + ".png");
                        BufferedImage img = new BufferedImage(appMainPanel.getWidth(), appMainPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                        appMainPanel.paint(img.getGraphics());
                        BufferedImage croppedImg = img.getSubimage(0, 0, appMainPanel.getImg().getWidth(), appMainPanel.getImg().getHeight());
                        ImageIO.write(croppedImg, "png", selectedFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class rotateItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mainImage != null && appRotatePanel == null) {
                appRotatePanel = new AppRotatePanel();
                appRotatePanel.jSlider.addChangeListener(e -> {
                    appMainPanel.setRotationDegree(appRotatePanel.jSlider.getValue());
                    appMainPanel.repaint();
                    AppFrame.this.repaint();
                });
                AppFrame.this.add(appRotatePanel);
                AppFrame.this.setVisible(true);
                appMainPanel.repaint();
                AppFrame.this.repaint();
            }
        }
    }

    private class colorItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mainImage != null && appRedSlider == null && appGreenSlider == null && appBlueSlider == null) {
                appRedSlider = new AppColor();
                appBlueSlider = new AppColor();
                appGreenSlider = new AppColor();
                appRedSlider.jSlider.addChangeListener(e -> {
                    for (int i = 0; i < mainImage.getWidth(); i++) {
                        for (int j = 0; j < mainImage.getHeight(); j++) {
                            int p = mainImage.getRGB(i, j);
                            int alpha = (p >> 24) & 0xff;
                            int green = (p >> 8) & 0xff;
                            int blue = (p) & 0xff;
                            mainImage.setRGB(i, j, colorToRGB(alpha, appRedSlider.jSlider.getValue(), green, blue));
                            appMainPanel.repaint();
                            AppFrame.this.repaint();
                        }
                    }
                });
                appGreenSlider.jSlider.addChangeListener(e -> {
                    for (int i = 0; i < mainImage.getWidth(); i++) {
                        for (int j = 0; j < mainImage.getHeight(); j++) {
                            int p = mainImage.getRGB(i, j);
                            int alpha = (p >> 24) & 0xff;
                            int red = (p >> 16) & 0xff;
                            int blue = (p) & 0xff;
                            mainImage.setRGB(i, j, colorToRGB(alpha, red, appGreenSlider.jSlider.getValue(), blue));
                            appMainPanel.repaint();
                            AppFrame.this.repaint();
                        }
                    }
                });
                appBlueSlider.jSlider.addChangeListener(e -> {
                    for (int i = 0; i < mainImage.getWidth(); i++) {
                        for (int j = 0; j < mainImage.getHeight(); j++) {
                            int p = mainImage.getRGB(i, j);
                            int alpha = (p >> 24) & 0xff;
                            int red = (p >> 16) & 0xff;
                            int green = (p >> 8) & 0xff;
                            mainImage.setRGB(i, j, colorToRGB(alpha, red, green, appBlueSlider.jSlider.getValue()));
                            appMainPanel.repaint();
                            AppFrame.this.repaint();
                        }
                    }
                });
                appRedSlider.setBorder(BorderFactory.createTitledBorder("Red"));
                appBlueSlider.setBorder(BorderFactory.createTitledBorder("Blue"));
                appGreenSlider.setBorder(BorderFactory.createTitledBorder("Green"));
                AppFrame.this.add(appRedSlider);
                AppFrame.this.add(appGreenSlider);
                AppFrame.this.add(appBlueSlider);
                AppFrame.this.setVisible(true);
            }
        }

        private int colorToRGB(int alpha, int red, int green, int blue) {
            int newPixel = 0;
            newPixel += alpha;
            newPixel = newPixel << 8;
            newPixel += red;
            newPixel = newPixel << 8;
            newPixel += green;
            newPixel = newPixel << 8;
            newPixel += blue;
            return newPixel;
        }
    }

    private class addTextItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (jTextField == null && mainImage != null) {
                jTextField = new JTextField("Click Enter to add the text...");
                btm = new JButton("Enter");
                jTextField.setPreferredSize(new Dimension(300, 50));
                AppFrame.this.add(jTextField);
                AppFrame.this.add(btm);
                btm.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String textFieldValue = jTextField.getText();
                        JLabel jLabel = new JLabel(textFieldValue);
                        jLabel.setBounds(300, 300, 300, 50);
                        appMainPanel.add(jLabel);
                        jLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                super.mousePressed(e);
                                Component comp = (Component) e.getSource();
                                initialLoc = comp.getLocation();
                                initialLocOnScreen = e.getLocationOnScreen();
                                repaint();
                            }

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                super.mouseClicked(e);
                                JPanel panel = new JPanel();
                                JLabel jLabel1 = new JLabel("what do you want to do with this text?");
                                JCheckBox checkbox1 = new JCheckBox("Font Size");
                                JCheckBox checkbox2 = new JCheckBox("Color");
                                JCheckBox checkbox3 = new JCheckBox("Edit text");
                                JCheckBox checkbox4 = new JCheckBox("Delete");

                                panel.add(jLabel1);
                                panel.add(checkbox1);
                                panel.add(checkbox2);
                                panel.add(checkbox3);
                                panel.add(checkbox4);

                                checkbox1.addChangeListener(e19 -> {
                                    if (checkbox1.isSelected()) {
                                        String string = JOptionPane.showInputDialog("font size:");
                                        if (string != null) {
                                            int fontSize = Integer.parseInt(string);
                                            jLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
                                        }
                                        checkbox1.setSelected(false);
                                    }
                                });

                                checkbox2.addChangeListener(e18 -> {
                                    if (checkbox2.isSelected()) {
                                        JPanel jPanel = new JPanel();
                                        JLabel jLabel2 = new JLabel("Choose your color:");
                                        JButton button1 = new JButton("Black");
                                        JButton button2 = new JButton("Blue");
                                        JButton button3 = new JButton("Red");
                                        JButton button4 = new JButton("Yellow");
                                        JButton button5 = new JButton("Green");
                                        JButton button6 = new JButton("Pink");
                                        JButton button7 = new JButton("Orange");
                                        JButton button8 = new JButton("Magenta");

                                        jPanel.add(jLabel2);
                                        jPanel.add(button1);
                                        jPanel.add(button2);
                                        jPanel.add(button3);
                                        jPanel.add(button4);
                                        jPanel.add(button5);
                                        jPanel.add(button6);
                                        jPanel.add(button7);
                                        jPanel.add(button8);

                                        button1.addActionListener(e17 -> jLabel.setForeground(Color.black));

                                        button2.addActionListener(e16 -> jLabel.setForeground(Color.blue));

                                        button3.addActionListener(e15 -> jLabel.setForeground(Color.red));

                                        button4.addActionListener(e14 -> jLabel.setForeground(Color.yellow));

                                        button5.addActionListener(e13 -> jLabel.setForeground(Color.green));

                                        button6.addActionListener(e12 -> jLabel.setForeground(Color.pink));

                                        button7.addActionListener(e1 -> jLabel.setForeground(Color.orange));

                                        button8.addActionListener(e181 -> jLabel.setForeground(Color.magenta));

                                        JOptionPane.showMessageDialog(null, jPanel);

                                    }
                                    checkbox2.setSelected(false);
                                });

                                checkbox3.addChangeListener(e111 -> {
                                    if (checkbox3.isSelected()) {
                                        String string = JOptionPane.showInputDialog("New text:");
                                        if (string != null) {
                                            jLabel.setText(string);
                                        }
                                        checkbox3.setSelected(false);
                                    }
                                });

                                checkbox4.addChangeListener(e110 -> {
                                    if (checkbox4.isSelected()) {
                                        jLabel.setVisible(false);
                                        checkbox4.setSelected(false);
                                    }
                                });

                                JOptionPane.showMessageDialog(null, panel);
                            }

                            @Override
                            public void mouseReleased(MouseEvent e) {
                                super.mouseReleased(e);
                                Component comp = (Component) e.getSource();
                                Point locOnScreen = e.getLocationOnScreen();
                                int x = locOnScreen.x - initialLocOnScreen.x + initialLoc.x;
                                int y = locOnScreen.y - initialLocOnScreen.y + initialLoc.y;
                                comp.setLocation(x, y);
                                repaint();
                            }
                        });
                        jLabel.addMouseMotionListener(new MouseMotionAdapter() {
                            @Override
                            public void mouseDragged(MouseEvent e) {
                                super.mouseDragged(e);
                                Component comp = (Component) e.getSource();
                                Point locOnScreen = e.getLocationOnScreen();
                                int x = locOnScreen.x - initialLocOnScreen.x + initialLoc.x;
                                int y = locOnScreen.y - initialLocOnScreen.y + initialLoc.y;
                                comp.setLocation(x, y);
                                repaint();
                            }
                        });
                        appMainPanel.setVisible(true);
                        appMainPanel.repaint();
                        AppFrame.this.repaint();
                        AppFrame.this.setVisible(true);
                    }
                });
                AppFrame.this.repaint();
                AppFrame.this.setVisible(true);
            }
        }
    }

    public class filtersItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainImage != null) {
                JPanel panel = new JPanel();
                JLabel jLabel = new JLabel("Choose your filter:");
                JButton button1 = new JButton("Crystallize");
                JButton button2 = new JButton("Invert");
                JButton button3 = new JButton("Diffuse");
                JButton button4 = new JButton("Dither");
                JButton button5 = new JButton("Exposure");
                JButton button6 = new JButton("Gray");
                JButton button7 = new JButton("Gray scale");
                JButton button8 = new JButton("Mask");
                JButton button9 = new JButton("Poster ize");
                JButton button10 = new JButton("Solar ize");

                panel.add(jLabel);
                panel.add(button1);
                panel.add(button2);
                panel.add(button3);
                panel.add(button4);
                panel.add(button5);
                panel.add(button6);
                panel.add(button7);
                panel.add(button8);
                panel.add(button9);
                panel.add(button10);

                button1.addActionListener(e110 -> {
                    CrystallizeFilter crystallizeFilter = new CrystallizeFilter();
                    crystallizeFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button2.addActionListener(e19 -> {
                    InvertFilter invertFilter = new InvertFilter();
                    invertFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button3.addActionListener(e18 -> {
                    DiffuseFilter diffuseFilter = new DiffuseFilter();
                    diffuseFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button4.addActionListener(e17 -> {
                    DitherFilter ditherFilter = new DitherFilter();
                    ditherFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button5.addActionListener(e16 -> {
                    ExposureFilter exposureFilter = new ExposureFilter();
                    exposureFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button6.addActionListener(e15 -> {
                    GrayFilter grayFilter = new GrayFilter();
                    grayFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button7.addActionListener(e14 -> {
                    GrayscaleFilter grayscaleFilter = new GrayscaleFilter();
                    grayscaleFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button8.addActionListener(e13 -> {
                    MaskFilter maskFilter = new MaskFilter();
                    maskFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button9.addActionListener(e12 -> {
                    PosterizeFilter posterizeFilter = new PosterizeFilter();
                    posterizeFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                button10.addActionListener(e1 -> {
                    SolarizeFilter solarizeFilter = new SolarizeFilter();
                    solarizeFilter.filter(mainImage, mainImage);
                    AppFrame.this.repaint();
                });

                JOptionPane.showMessageDialog(null, panel);
            }
        }
    }

    public class addStickerItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainImage != null) {
                JPanel panel = new JPanel();
                JLabel jLabel = new JLabel("Choose your Sticker:");
                JButton button1 = new JButton("Sticker 1");
                JButton button2 = new JButton("Sticker 2");
                JButton button3 = new JButton("Sticker 3");
                JButton button4 = new JButton("Sticker 4");
                JButton button5 = new JButton("Sticker 5");
                JButton button6 = new JButton("Sticker 6");
                JButton button7 = new JButton("Sticker 7");
                JButton button8 = new JButton("Sticker 8");
                JButton button9 = new JButton("Sticker 9");
                JButton button10 = new JButton("Sticker 10");

                panel.add(jLabel);
                panel.add(button1);
                panel.add(button2);
                panel.add(button3);
                panel.add(button4);
                panel.add(button5);
                panel.add(button6);
                panel.add(button7);
                panel.add(button8);
                panel.add(button9);
                panel.add(button10);

                ImageIcon imageIcon1 = new ImageIcon("Monztars_PNG\\Monztars_1.png");
                ImageIcon imageIcon2 = new ImageIcon("Monztars_PNG\\Monztars_2.png");
                ImageIcon imageIcon3 = new ImageIcon("Monztars_PNG\\Monztars_3.png");
                ImageIcon imageIcon4 = new ImageIcon("Monztars_PNG\\Monztars_4.png");
                ImageIcon imageIcon5 = new ImageIcon("Monztars_PNG\\Monztars_5.png");
                ImageIcon imageIcon6 = new ImageIcon("Monztars_PNG\\Monztars_6.png");
                ImageIcon imageIcon7 = new ImageIcon("Monztars_PNG\\Monztars_7.png");
                ImageIcon imageIcon8 = new ImageIcon("Monztars_PNG\\Monztars_8.png");
                ImageIcon imageIcon9 = new ImageIcon("Monztars_PNG\\Monztars_9.png");
                ImageIcon imageIcon10 = new ImageIcon("Monztars_PNG\\Monztars_10.png");

                JLabel jLabel1 = new JLabel();
                JLabel jLabel2 = new JLabel();
                JLabel jLabel3 = new JLabel();
                JLabel jLabel4 = new JLabel();
                JLabel jLabel5 = new JLabel();
                JLabel jLabel6 = new JLabel();
                JLabel jLabel7 = new JLabel();
                JLabel jLabel8 = new JLabel();
                JLabel jLabel9 = new JLabel();
                JLabel jLabel10 = new JLabel();

                jLabel1.setIcon(imageIcon1);
                jLabel2.setIcon(imageIcon2);
                jLabel3.setIcon(imageIcon3);
                jLabel4.setIcon(imageIcon4);
                jLabel5.setIcon(imageIcon5);
                jLabel6.setIcon(imageIcon6);
                jLabel7.setIcon(imageIcon7);
                jLabel8.setIcon(imageIcon8);
                jLabel9.setIcon(imageIcon9);
                jLabel10.setIcon(imageIcon10);

                jLabel1.setLocation(300 , 200);
                jLabel2.setLocation(300 , 200);
                jLabel3.setLocation(300 , 200);
                jLabel4.setLocation(300 , 200);
                jLabel5.setLocation(300 , 200);
                jLabel6.setLocation(300 , 200);
                jLabel7.setLocation(300 , 200);
                jLabel8.setLocation(300 , 200);
                jLabel9.setLocation(300 , 200);
                jLabel10.setLocation(300 , 200);


                button1.addActionListener(e110 -> {
                    appMainPanel.add(jLabel1);
                    repaint();
                    AppFrame.this.repaint();
                });

                JOptionPane.showMessageDialog(null, panel);

                //be dalil kambood vaght add sticker jkamel nashod
            }
        }
    }

    public class cropItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainImage != null) {
                appMainPanel.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent evt) {
                        xPressed = evt.getX();
                        yPressed = evt.getY();
                    }

                    public void mouseReleased(MouseEvent evt) {
                        xDest = evt.getX();
                        yDest = evt.getY();
                        if (yDest > yPressed) {
                            appMainPanel.setyPressed(yPressed);
                            appMainPanel.setyDest(yDest);
                        } else {
                            appMainPanel.setyPressed(yDest);
                            appMainPanel.setyDest(yPressed);
                        }
                        if (xDest > xPressed) {
                            appMainPanel.setxPressed(xPressed);
                            appMainPanel.setxDest(xDest);
                        } else {
                            appMainPanel.setxPressed(xDest);
                            appMainPanel.setxDest(xPressed);
                        }
                        appMainPanel.setVisible(true);
                        appMainPanel.repaint();
                        AppFrame.this.repaint();
                    }
                });
                appMainPanel.addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent evt) {
                        xDest = evt.getX();
                        yDest = evt.getY();
                        appMainPanel.getGraphics().setColor(Color.WHITE);
                        if (xDest > xPressed && yDest > yPressed) {
                            appMainPanel.getGraphics().drawRect(xPressed, yPressed, xDest - xPressed, yDest - yPressed);
                        } else if (xDest > xPressed && yDest < yPressed) {
                            appMainPanel.getGraphics().drawRect(xPressed, yDest, xDest - xPressed, yPressed - yDest);
                        } else if (xDest < xPressed && yDest > yPressed) {
                            appMainPanel.getGraphics().drawRect(xDest, yPressed, xPressed - xDest, yDest - yPressed);
                        } else if (xDest < xPressed && yDest < yPressed) {
                            appMainPanel.getGraphics().drawRect(xDest, yDest, xPressed - xDest, yPressed - yDest);
                        }
                        appMainPanel.repaint();
                        appMainPanel.setVisible(true);
                        AppFrame.this.repaint();
                        AppFrame.this.setVisible(true);
                    }
                });
            }
        }
    }
}
