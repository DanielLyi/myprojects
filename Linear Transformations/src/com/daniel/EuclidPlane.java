package com.daniel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

public class EuclidPlane extends JComponent {
    public static final int COMPONENT_WIDTH = 800;
    public static final int COMPONENT_HEIGHT = 800;
    public static final double MAX_CART_X = 6;
    public static final double MAX_CART_Y = MAX_CART_X;
    /**
     * Time for animation to go (in milliseconds)
     */
    public static final long ANIMATION_TIME = 1200L;
    public static final int STEPS = 60;
    public static double[] MATRIX = new double[]
            {3, 1, 4, -3};
    private boolean applied = false;
    private double tempDouble = 0;
    private int tempDigitCount = 0;
    private int currentEntry = 0;
    private boolean entering = false;
    private ArrayList<Vector2D> vectors2D;
    private ArrayList<Vector2D> toDraw;
    private boolean showDet = true;
    private JButton applyButton;

    public EuclidPlane() {
        init();
    }

    public static double xInScreenCoords(double cartX) {
        return cartX / MAX_CART_X * ((double) COMPONENT_WIDTH) / 2 + ((double) COMPONENT_WIDTH) / 2;
    }

    public static double xLengthInPixels(double xCartLength) {
        return xCartLength / MAX_CART_X * ((double) COMPONENT_WIDTH) / 2;
    }

    public static double yInScreenCoords(double cartY) {
        return ((double) COMPONENT_HEIGHT) / 2 - cartY / MAX_CART_Y * ((double) COMPONENT_HEIGHT) / 2;
    }

    public static double yLengthInPixels(double yCartLength) {
        return yCartLength / MAX_CART_Y * ((double) COMPONENT_HEIGHT) / 2;
    }

    public static Point2D.Double pointInScreenCoords(double cartX, double cartY) {
        return new Point2D.Double(xInScreenCoords(cartX), yInScreenCoords(cartY));
    }

    private void init() {
        toDraw = new ArrayList<>();


        applyButton = new JButton("Apply");
        applyButton.setBounds(0, 0, 100, 50);
        applyButton.setBackground(Color.WHITE.darker().darker().darker());
        applyButton.setFocusable(false);
        applyButton.setRolloverEnabled(false);

        applyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!applied) {
                    applyButton.setEnabled(false);
                    applyButton.setText("Return");
                    applied = true;
                    Vector2D.animate(vectors2D);

                } else {
                    toDraw.clear();
                    applyButton.setText("Apply");/*
                    for (int i = 0; i < 4; i++) {
                        MATRIX[i] = new Random().nextInt(10) - 4;
                    }*/
                    applied = false;
                }
                repaint();
            }
        });


        add(applyButton);

        InputMap inputMap = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        KeyStroke[] keyStrokes = new KeyStroke[10];
        ActionMap actionMap = this.getActionMap();
        for (int i = 0; i <= 9; i++) {
            keyStrokes[i] = KeyStroke.getKeyStroke("" + i);
            inputMap.put(keyStrokes[i], i + "");
            int p = i;
            actionMap.put(i + "", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String istr = String.valueOf(p);
                    tempDouble = Double.parseDouble(tempDouble + "" + istr);
                    tempDigitCount++;
                    entering = true;
                    repaint();
                    System.out.println(tempDouble);
                }
            });
        }
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        inputMap.put(enter, "accept");
        actionMap.put("accept", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MATRIX[currentEntry] = tempDouble * pow(10, 1 + tempDigitCount);
                tempDouble = 0;
                tempDigitCount = 0;
                currentEntry++;
                if (currentEntry > 3) currentEntry = 0;
                entering = false;
                repaint();
            }
        });


        JButton changeEntriesButton = new JButton("Change entries");
        changeEntriesButton.addActionListener(event -> {
            applyButton.setEnabled(false);

            BlockingQueue<Double> blockingQueue = new ArrayBlockingQueue<>(4);
            while (true) {

            }
        });


        Random r = new Random();
        vectors2D = new ArrayList<>();
        vectors2D.add(new Vector2D(1, 0, Color.GREEN, false)); //i hat
        vectors2D.add(new Vector2D(0, 1, Color.RED, false)); //j hat
        for (int i = 0; i <= 19; i++) {
            for (int j = 0; j <= 19; j++) {
                vectors2D.add(new Vector2D(((double) i - 9) / 2, ((double) j - 9) / 2, new Color(
                        100 + r.nextInt(21) - 10,
                        100 + r.nextInt(21) - 10,
                        100 + r.nextInt(21) - 10), true));
            }
        }


        vectors2D.add(new Vector2D(2, 3, Color.ORANGE, false));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setPaint(Color.WHITE.darker());
        g2.draw(new Line2D.Double(
                pointInScreenCoords(0, -MAX_CART_Y), pointInScreenCoords(0, MAX_CART_Y)));
        g2.draw(new Line2D.Double(
                pointInScreenCoords(-MAX_CART_X, 0), pointInScreenCoords(MAX_CART_X, 0)
        ));

        for (int i = -(int) MAX_CART_X; i <= (int) MAX_CART_X; i++) {
            g2.draw(new Line2D.Double(
                    pointInScreenCoords(i, -0.1), pointInScreenCoords(i, 0.1)
            ));
            g2.drawString(i + "", (int) xInScreenCoords(i + 0.1), (int) yInScreenCoords(-0.3));
        }

        for (int i = -(int) MAX_CART_Y; i <= (int) MAX_CART_Y; i++) {
            g2.draw(new Line2D.Double(
                    pointInScreenCoords(-0.1, i), pointInScreenCoords(0.1, i)
            ));
            if (i != 0) {
                g2.drawString(i + "", (int) xInScreenCoords(0.1), (int) yInScreenCoords(i - 0.3));
            }
        }


        if (!applied) {

            for (Vector2D vector2D : vectors2D) {
                vector2D.draw(g2);
            }
        }
        if (showDet) {
            Vector2D iHat;
            Vector2D jHat;
            if (toDraw.size() > 0) {
                iHat = toDraw.get(0);
                jHat = toDraw.get(1);
            } else {
                iHat = vectors2D.get(0);
                jHat = vectors2D.get(1);
            }

            double curDet = iHat.getCartX() * jHat.getCartY()
                    - jHat.getCartX() * iHat.getCartY();
            if (curDet > 0) {
                g2.setPaint(new Color(255, 160, 31, 98));
            } else {
                g2.setPaint(new Color(255, 160, 31, 62));
            }
            if (curDet != 0) {
                g2.fillPolygon(new int[]{(int) xInScreenCoords(0),
                                (int) xInScreenCoords(iHat.getCartX()),
                                (int) xInScreenCoords(iHat.getCartX() + jHat.getCartX()),
                                (int) xInScreenCoords(jHat.getCartX())},
                        new int[]{(int) yInScreenCoords(0),
                                (int) yInScreenCoords(iHat.getCartY()),
                                (int) yInScreenCoords(iHat.getCartY() + jHat.getCartY()),
                                (int) yInScreenCoords(jHat.getCartY())}, 4);
            }
        }
        for (Vector2D vector2D : toDraw) {

            vector2D.draw(g2);
        }

        g2.setPaint(Color.WHITE.darker());



        /*Vector2D iHat = new Vector2D(1, 0, Color.GREEN);//i hat
        iHat.draw(g2, false, applied);
        mainVectors.add(iHat);


        var jHat = new Vector2D(0, 1, Color.RED);//j hat
        jHat.draw(g2, false, applied);
        mainVectors.add(jHat);

        var v1 = new Vector2D(3, 2, Color.ORANGE);// a vector
        v1.draw(g2, false, applied);
        mainVectors.add(v1);*/


        ///////////////Showing the Matrix////////////////////
        g2.draw(new Arc2D.Double(new Rectangle2D.Double(
                xInScreenCoords(-5), yInScreenCoords(5),
                xInScreenCoords(-4), yInScreenCoords(4)
        ),
                135, 90, Arc2D.OPEN));

        g2.draw(new Arc2D.Double(new Rectangle2D.Double(
                xInScreenCoords(-5), yInScreenCoords(5),
                xInScreenCoords(-4), yInScreenCoords(4)
        ),
                -45, 90, Arc2D.OPEN));

        g2.setFont(g2.getFont().deriveFont(24f));

        DecimalFormat format = new DecimalFormat("#.##");

        if (!entering) {
            g2.drawString(format.format(MATRIX[0]) + "", (int) xInScreenCoords(-4.5),
                    (int) yInScreenCoords(4.25));

            g2.drawString(format.format(MATRIX[1]) + "", (int) xInScreenCoords(-3.6),
                    (int) yInScreenCoords(4.25));

            g2.drawString(format.format(MATRIX[2]) + "", (int) xInScreenCoords(-4.5),
                    (int) yInScreenCoords(3.55));

            g2.drawString(format.format(MATRIX[3]) + "", (int) xInScreenCoords(-3.6),
                    (int) yInScreenCoords(3.55));
        } else {
            int x;
            int y;
            switch (currentEntry) {
                case 0:
                    x = (int) xInScreenCoords(-4.5);
                    y = (int) yInScreenCoords(4.25);
                    break;
                case 1:
                    x = (int) xInScreenCoords(-3.6);
                    y = (int) yInScreenCoords(4.25);
                    break;
                case 2:
                    x = (int) xInScreenCoords(-4.5);
                    y = (int) yInScreenCoords(3.55);
                    break;
                case 3:
                    x = (int) xInScreenCoords(-3.6);
                    y = (int) yInScreenCoords(3.55);
                    break;
                default:
                    x = 0;
                    y = 0;
            }
            g2.setPaint(Color.RED);
            g2.drawString(format.format(tempDouble * pow(10, 1 + tempDigitCount)),
                    x, y);
            g2.setPaint(Color.WHITE.darker());
        }
        /////////////////////////////////////////////////////


    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT);
    }

    public void addToDraw(ArrayList<Vector2D> v) {
        toDraw.addAll(v);
    }

    public void removeFromDraw(ArrayList<Vector2D> v) {
        toDraw.removeAll(v);
    }

    public void removeFromDrawAll() {
        toDraw.clear();
    }

    public void enableApplyButton() {
        applyButton.setEnabled(true);
    }
}
