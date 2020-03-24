package com.dan;

public enum PieceType {
    PAWN("pawn"), KNIGHT("knight"), BISHOP("bishop"),
    ROOK("rook"), QUEEN("queen"), KING("king");

    private String name;

    PieceType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
