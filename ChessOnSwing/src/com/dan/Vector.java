package com.dan;

public class Vector {
    private int x;
    private int y;
    private boolean illegal = false;

    public Vector(int x, int y) throws Exception {
        if (x < 1 || x > 8 || y < 1 || y > 8) {
            if (x == -2 && y == -2) {
                this.illegal = true;
            } else {
                throw new Exception("Out of range");
            }
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isIllegal() {
        return this.illegal;
    }



    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector){
            if(this.x == ((Vector) obj).x&&this.y==((Vector) obj).y) return true;
        }
        return false;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
