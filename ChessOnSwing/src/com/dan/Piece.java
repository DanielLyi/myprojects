package com.dan;

public class Piece {
    public static final int YELLOW = 1;
    public static final int BROWN = 2;

    private Vector vector;
    private int team;
    private PieceType type;

    public Piece(int team, Vector v, PieceType type) throws Exception {
        if(team!=1&&team!=2&&team!=-1){
            throw new Exception("Out of bounds");
        }
        this.team = team;
        this.vector = v;
        this.type = type;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public PieceType getType() {
        return type;
    }

    public int getTeam() {
        return team;
    }
}

