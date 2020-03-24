package com.univliakh;

import java.awt.*;
import java.awt.geom.Point2D;

public class ColoredPoint2D extends Point2D.Double {

    private Color color;

    public ColoredPoint2D(double x, double y, Color color){
        super(x,y);
        this.color = color;
    }

    @Override
    public double getX() {
        return super.x;
    }

    @Override
    public double getY() {
        return super.y;
    }

    @Override
    public void setLocation(double x, double y) {
        super.x = x;
        super.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
