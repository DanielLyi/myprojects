package com.univliakh;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class CartPlane extends JComponent {
    private MyFrame frameContent;
    private Rectangle2D.Double planePart;
    private int partSide = 600;
    private int maxNumber = 12;

    public static int pointRad = 4;

    public CartPlane(MyFrame content) {
        frameContent = content;
        init();
    }

    private void init() {
        planePart = new Rectangle2D.Double(0, 0, partSide, partSide);
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.WHITE);
        g2.fill(planePart); //filling white bg

        g2.setPaint(Color.LIGHT_GRAY);
        for (int i = -maxNumber; i <= maxNumber; i++) { //Drawing grid
            g2.drawLine((int) windowX(i), (int) windowY(-maxNumber), (int) windowX(i), (int) windowY(maxNumber));
        }
        for (int j = -maxNumber; j <= maxNumber; j++) {
            g2.drawLine((int) windowX(-maxNumber), (int) windowY(j), (int) windowX(maxNumber), (int) windowY(j));
        }


        g2.setPaint(Color.BLACK);
        /*for (int i = -maxNumber + 1; i < maxNumber; i++) { //Drawing dashes
            g2.drawLine((int)windowX(i),(int)windowY(-0.2),(int)windowX(i),(int)windowY(0.2));
        }
        for (int j = -maxNumber + 1; j < maxNumber; j++) {
            g2.drawLine((int) windowX(-0.2) ,(int)  windowY(j),(int) windowX(0.2) ,(int) windowY(j) );
        }*/


        // Drawing axis
        g2.drawLine((int) windowX(0), (int) windowY(maxNumber), (int) windowX(0), (int) windowY(-maxNumber));
        g2.drawLine((int) windowX(-maxNumber), (int) windowY(0), (int) windowX(maxNumber), (int) windowY(0));

        for (int i = -maxNumber + 1; i < maxNumber; i++) { //Real number line
            g2.drawString("" + i, (int) (windowX(i) + partSide / (maxNumber * 16)), (int)
                    (windowY(0) + partSide / (maxNumber * 4)));
        }

        for (int j = -maxNumber + 1; j < 0; j++) { //Imaginary number line
            g2.drawString(j + "i", (int) (windowX(0) + partSide / (maxNumber * 16)), (int)
                    (windowY(j) + partSide / (maxNumber * 4)));
        }
        for (int j = 1; j < maxNumber; j++) {// ...
            g2.drawString(j + "i", (int) (windowX(0) + partSide / (maxNumber * 16)), (int)
                    (windowY(j) + partSide / (maxNumber * 4)));
        }

        g2.setFont(getFont().deriveFont(Font.BOLD));
        g2.drawString("Re", (int) (windowX(maxNumber) - partSide / (maxNumber * 4)), (int) (windowY(0) + partSide /
                (maxNumber * 4)));
        g2.drawString("Im", (int) (windowX(0) + partSide / (maxNumber * 8)), (int) (windowY(maxNumber) +
                partSide / (maxNumber * 4)));


    }

    protected double cartX(double windowX) {
        double newX;
        newX = (windowX - partSide * 0.5) / (partSide * 0.5) * maxNumber;
        return newX;
    }

    protected double cartY(double windowY) {
        double newY;
        double downOldY = partSide - windowY;
        newY = (downOldY - partSide * 0.5) / (partSide * 0.5) * maxNumber;
        return newY;
    }

    protected double windowX(double cartX) {
        double newX;
        newX = cartX / maxNumber * (partSide * 0.5) + partSide * 0.5;
        return newX;
    }

    protected double windowY(double cartY) {
        double newY;
        newY = partSide * 0.5 - cartY / maxNumber * (partSide * 0.5);
        return newY;
    }

    protected void setMaxNumber(int maxNumber){
        this.maxNumber = maxNumber;
    }

    protected boolean pointIsInPart(Point2D.Double p){
        return planePart.contains(p);
    }

}
