package com.game;

import java.awt.*;

public class Piece {
    static int NO_ORIENTATION = 0;
    static int VERTICAL = 1;
    static int HORIZONTAL = 2;
    private int length;
    private int orientation;
    private Color color1;
    private Color color2;
    private int X;
    private int Y;
    private boolean[][] filledRects;
    private Board content;

    Piece(int X, int Y, int length, int orientation, Color color1, Board content){
        this.X = X;
        this.Y = Y;
        this.length = length;
        this.orientation = orientation;
        this.color1 = color1;
        this.content = content;
        setFieldRects();
    }

    Piece(int X, int Y, int length, int orientation, Color color1, Color color2, Board content){
        this.X = X;
        this.Y = Y;
        this.length = length;
        this.orientation = orientation;
        this.color1 = color1;
        this.color2=color2;
        this.content = content;
        setFieldRects();
    }

    void setFieldRects() {
        filledRects = new boolean[content.getNUM_OF_ROWS()][content.getNUM_OF_COLUMNS()];
        filledRects[Y][X] = true;
        if(length==2){
            if (orientation == Piece.HORIZONTAL){
                filledRects[Y][X+1]=true;
            } else if(orientation == Piece.VERTICAL){
                filledRects[Y+1][X]=true;
            }
        }
    }

    void allRectsFalse(){
        filledRects = new boolean[content.getNUM_OF_ROWS()][content.getNUM_OF_COLUMNS()];
    }

    int getLength() {
        return length;
    }

    void setLength(int length) {
        this.length = length;
    }

    int getOrientation() {
        return orientation;
    }

    Color getColor1() {
        return color1;
    }

    Color getColor2() {
        return color2;
    }

    int getX() {
        return X;
    }

    void setX(int x) {
        allRectsFalse();
        this.X = x;


    }

    int getY() {
        return Y;
    }

    void setY(int y) {
        allRectsFalse();
        this.Y = y;


    }

    boolean[][] getFilledRects() {
        return filledRects;
    }

    @Override
    public String toString() {
        return "[com.game.Piece:"+"x=" + X +  ";y=" + Y +  ";length="+length+";orientation="+orientation+"color1="+color1+"]";
    }
}
