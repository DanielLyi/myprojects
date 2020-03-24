package com.daniel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import static com.daniel.MyFrame.EUCLID_PLANE;
import static java.lang.Math.*;
import static java.lang.Math.sin;
import static com.daniel.EuclidPlane.*;
import static com.daniel.EuclidPlane.MATRIX;


public class Vector2D {
    private final double cartX;
    private final double cartY;
    private final Color color;
    private boolean onlyPoint;

    public Vector2D(double cartX, double cartY, boolean onlyPoint) {
        this(cartX, cartY, null, onlyPoint);
    }

    public Vector2D(double cartX, double cartY, Color color, boolean onlyPoint) {
        this.cartX = cartX;
        this.cartY = cartY;
        this.color = color;
        this.onlyPoint = onlyPoint;
    }

    public static void animate(List<Vector2D> vector2DList) {
        Animator animator = new Animator(vector2DList);
        animator.execute();
    }

    public boolean isOnlyPoint() {
        return onlyPoint;
    }

    public void draw(Graphics2D g2) {
        Color previous = (Color) g2.getPaint();
        if (color != null) g2.setPaint(color);
        double cartXToShow = cartX;
        double cartYToShow = cartY;
        if (onlyPoint || (cartXToShow == 0 && cartYToShow == 0)) {
            g2.fill(new Ellipse2D.Double(xInScreenCoords(cartXToShow - 0.05),
                    yInScreenCoords(cartYToShow + 0.05),
                    xLengthInPixels(0.1), yLengthInPixels(0.1)));
        } else {
            g2.drawLine((int) xInScreenCoords(0), (int) yInScreenCoords(0),
                    (int) xInScreenCoords(cartXToShow), (int) yInScreenCoords(cartYToShow));
            double theta0 = atan2(cartYToShow, cartXToShow);

            double absdTheta = PI / 6;

            double theta1 = theta0 - absdTheta;
            double theta2 = theta0 + absdTheta;

            double arrowLength = 0.2;

            int[] x = new int[3];
            int[] y = new int[3];

            x[0] = (int) xInScreenCoords(cartXToShow);
            y[0] = (int) yInScreenCoords(cartYToShow);

            for (int i = 1; i <= 2; i++) {
                double curAngle = 0;
                switch (i) {
                    case 1:
                        curAngle = theta1;
                        break;
                    case 2:
                        curAngle = theta2;
                        break;
                }

                double deltaX = -cos(curAngle) * arrowLength;
                double deltaY = -sin(curAngle) * arrowLength;

                x[i] = (int) xInScreenCoords(cartXToShow + deltaX);
                y[i] = (int) yInScreenCoords(cartYToShow + deltaY);

            }

            Polygon triangle = new Polygon(x, y, 3);
            g2.fillPolygon(triangle);

        }
        g2.setPaint(previous);
    }

    public double getdTheta() {
        double angle1;
        angle1 = atan2(cartY, cartX); //(-PI;PI]
        double angle2;
        double newX = cartX * MATRIX[0] + cartY * MATRIX[1];
        double newY = cartX * MATRIX[2] + cartY * MATRIX[3];

        angle2 = atan2(newY, newX); //(-PI;PI]

        double diff;
        if (abs(angle2 - angle1) > PI) {
            if (angle2 > angle1) diff = angle2 - (angle1 + 2 * PI);
            else diff = (angle2 + 2 * PI) - angle1;
        } else {
            diff = angle2 - angle1;
        }
        return diff;
    }

    public double getdR() {
        double newX = cartX * MATRIX[0] + cartY * MATRIX[1];
        double newY = cartX * MATRIX[2] + cartY * MATRIX[3];

        double oldLength = sqrt(cartX * cartX + cartY * cartY);
        double newLength = sqrt(newX * newX + newY * newY);

        return newLength - oldLength;
    }

    public double getCartX() {
        return cartX;
    }

    public double getCartY() {
        return cartY;
    }

    private static class Animator extends SwingWorker<Void, ArrayList<Vector2D>> {

        private List<Vector2D> vector2DList;
        private ArrayList<Vector2D> previous = new ArrayList<>();
        private ArrayList<Vector2D> finalVectors = new ArrayList<>();

        private Animator(List<Vector2D> vector2DList) {
            this.vector2DList = vector2DList;
        }

        @Override
        public Void doInBackground() {


            for (int i = 0; i < STEPS; i++) {
                ArrayList<Vector2D> vectors = new ArrayList<>();
                for (Vector2D vector : vector2DList) {
                    double cartX = vector.getCartX();
                    double cartY = vector.getCartY();
                    Color color = vector.color;
                    double startTheta = atan2(cartY, cartX); //(-PI;PI]
                    double startR = sqrt(cartX * cartX + cartY * cartY);
                    double dTheta = vector.getdTheta();
                    double dR = vector.getdR();

                    double stepTheta = dTheta / STEPS;
                    double stepR = dR / STEPS;

                    double curTheta = startTheta + stepTheta * (i + 1);
                    double curR = startR + stepR * (i + 1);
                    double curX = curR * cos(curTheta);
                    double curY = curR * sin(curTheta);

                    vectors.add(new Vector2D(curX, curY, color, vector.isOnlyPoint()));
                    if (i == STEPS - 1) finalVectors.add(new Vector2D(curX, curY, color, vector.isOnlyPoint()));
                }
                publish(vectors);
                try {
                    Thread.sleep(ANIMATION_TIME / STEPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }


            return null;
        }

        public void process(List<ArrayList<Vector2D>> vectors2DList) {
            ArrayList<Vector2D> vectors2D = vectors2DList.get(0);
            EUCLID_PLANE.removeFromDraw(previous);
            EUCLID_PLANE.addToDraw(vectors2D);
            EUCLID_PLANE.repaint();
            previous.clear();
            previous.addAll(vectors2D);
        }

        public void done() {
            EUCLID_PLANE.removeFromDrawAll();
            EUCLID_PLANE.addToDraw(finalVectors);
            EUCLID_PLANE.repaint();
            EUCLID_PLANE.enableApplyButton();
        }


    }

}
