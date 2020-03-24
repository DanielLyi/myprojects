package com.game;

public enum Vector {
    UP(0,-1), DOWN(0,1), LEFT(-1,0), RIGHT(1,0), STOP(0,0);


    Vector(int x, int y){
        this.x = x;
        this.y  = y;
    }

    private int x;
    private int y;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
