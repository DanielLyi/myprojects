package com.univliakh;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;

public class InputPlane extends CartPlane {
    private LinkedList<Point2D.Double> points;
    private OutputPlane outputPlane;
    private Random r;
    private Color randSameColor;
    boolean redRev;
    boolean greenRev;
    boolean blueRev;
    public InputPlane(MyFrame content,OutputPlane outputPlane){
        super(content);
        points = new LinkedList<>();

        this.outputPlane = outputPlane;
        r = new Random();
        randSameColor = new Color(162,255, 130);
        initInput();
    }

    private void initInput(){
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseMotionHandler());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        if (!points.isEmpty()) {

            for (Point2D.Double p : points) {
                Ellipse2D.Double ellipse = new Ellipse2D.Double(p.x- pointRad,p.y- pointRad,
                        pointRad *2, pointRad *2);
                g2.setPaint( ( ( ColoredPoint2D ) ( p ) ).getColor() );
                g2.fill(ellipse);
            }
        }
    }

    private void newSameColor(){
        randSameColor = new Color(r.nextInt(256),r.nextInt(256),
                r.nextInt(256));
    }

    public void drawPoint(double x, double y, boolean inCartesian, boolean sameColor) {
            Point2D.Double currentPoint;
        if(inCartesian) {
            currentPoint = new ColoredPoint2D(windowX(x), windowY(y),Color.BLACK);
        } else{
            currentPoint = new ColoredPoint2D(x, y,Color.BLACK);
        }
        if(!pointIsInPart(currentPoint)){
            return;
        }

        if(!sameColor){
        ((ColoredPoint2D)(currentPoint)).setColor(new Color(r.nextInt(256),r.nextInt(256),
                r.nextInt(256)));}
        else{
            int newRed;
            if(!redRev) {
                newRed = randSameColor.getRed() + 1;
                if(newRed>255) {
                    newRed =255;
                    redRev = true;
                }
            } else{
                newRed = randSameColor.getRed() - 2;
                if(newRed<0) {
                    newRed = 0;
                    redRev = false;
                }
            }

            int newGreen;
            if(!greenRev) {
                newGreen = randSameColor.getGreen() -1;
                if(newGreen<0) {
                    newGreen =0;
                    greenRev = true;
                }
            } else{
                newGreen = randSameColor.getGreen() + 2;
                if(newGreen>255) {
                    newGreen = 255;
                    greenRev = false;
                }
            }

            int newBlue;
            if(!blueRev) {
                newBlue = randSameColor.getBlue() + 1;
                if(newBlue>255) {
                    newBlue = 255;
                    blueRev = true;
                }
            } else{
                newBlue = randSameColor.getBlue() - 1;
                if(newBlue<0) {
                    newBlue = 0;
                    blueRev = false;
                }
            }

            randSameColor = new Color(newRed,newGreen,newBlue);
            ((ColoredPoint2D)(currentPoint)).setColor(randSameColor);
        }

        points.add(currentPoint);
        repaint();
        outputPlane.drawPoints(this);
    }



    private class MouseHandler extends MouseAdapter{

            @Override
            public void mousePressed(MouseEvent e){

                newSameColor();
                drawPoint(e.getX(),e.getY(),false,false);
            }



    }

    private class MouseMotionHandler extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e) {
            drawPoint(e.getX(),e.getY(),false,true);
        }

    }

    public LinkedList<Point2D.Double> getPoints(){
        return points;
    }

    public void killAllPoints(){
        points.clear();
        repaint();
    }

}
