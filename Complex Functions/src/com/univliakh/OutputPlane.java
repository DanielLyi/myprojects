package com.univliakh;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class OutputPlane extends CartPlane {
    private LinkedList<Point2D.Double> points;
    private Equation eq;
    public OutputPlane(MyFrame content, Equation q) {
        super(content);
        eq = q;
        init();
    }

    private void init(){
        points = new LinkedList<>();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if(!points.isEmpty()){
            for (Point2D.Double p : points) {
                Ellipse2D.Double ellipse = new Ellipse2D.Double(p.x- pointRad,p.y- pointRad,
                        pointRad *2, pointRad *2);
                g2.setPaint( ( (ColoredPoint2D) (p) ).getColor() );
                g2.fill(ellipse);
            }
        }
    }

    public void drawPoints(InputPlane inputPlane){
        var oldPoints  = inputPlane.getPoints();
        points.clear();

        for (Point2D.Double oldP :
                oldPoints) {
            Color c = ((ColoredPoint2D)oldP).getColor();
            ComplexNumber oldComplexNumber = new ComplexNumber(cartX(oldP.x),cartY(oldP.y));
            ComplexNumber newComplexNumber = eq.equation(oldComplexNumber);
            points.add(new ColoredPoint2D(windowX(newComplexNumber.getRealPart()),
                    windowY(newComplexNumber.getImaginaryPart()),c));

        }



        repaint();
    }


    public void killAllPoints(){
        points.clear();
        repaint();
    }
}
