package com.dan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

import static com.dan.Constants.SQUARE_SIDE;
import static com.dan.Piece.BROWN;
import static com.dan.Piece.YELLOW;


public class Chessboard extends JComponent {

    private ArrayList<Piece> yellowPieces;
    private ArrayList<Piece> brownPieces;
    private int teamOnTurn;
    private boolean showAvailable = false;
    private ArrayList<Vector> availableSquares;
    private ArrayList<Vector> squaresToEat;
    private NullPiece nullPiece;
    private Piece currentPiece;

    public Chessboard() throws Exception {
        init();
    }

    private void init() throws Exception {
        yellowPieces = new ArrayList<>(16);
        brownPieces = new ArrayList<>(16);
        for (int i = 1; i <= 8; i++) { //adding pawns
            try {
                yellowPieces.add(new Piece(YELLOW, new Vector(i, 2), PieceType.PAWN));
                brownPieces.add(new Piece(BROWN, new Vector(i, 7), PieceType.PAWN));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try { //adding knights
            yellowPieces.add(new Piece(YELLOW, new Vector(2, 1), PieceType.KNIGHT));
            brownPieces.add(new Piece(BROWN, new Vector(2, 8), PieceType.KNIGHT));
            yellowPieces.add(new Piece(YELLOW, new Vector(7, 1), PieceType.KNIGHT));
            brownPieces.add(new Piece(BROWN, new Vector(7, 8), PieceType.KNIGHT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try { //adding bishops
            yellowPieces.add(new Piece(YELLOW, new Vector(3, 1), PieceType.BISHOP));
            brownPieces.add(new Piece(BROWN, new Vector(3, 8), PieceType.BISHOP));
            yellowPieces.add(new Piece(YELLOW, new Vector(6, 1), PieceType.BISHOP));
            brownPieces.add(new Piece(BROWN, new Vector(6, 8), PieceType.BISHOP));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try { //adding rooks
            yellowPieces.add(new Piece(YELLOW, new Vector(1, 1), PieceType.ROOK));
            brownPieces.add(new Piece(BROWN, new Vector(1, 8), PieceType.ROOK));
            yellowPieces.add(new Piece(YELLOW, new Vector(8, 1), PieceType.ROOK));
            brownPieces.add(new Piece(BROWN, new Vector(8, 8), PieceType.ROOK));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try { //adding queens and kings
            yellowPieces.add(new Piece(YELLOW, new Vector(4, 1), PieceType.QUEEN));
            brownPieces.add(new Piece(BROWN, new Vector(4, 8), PieceType.QUEEN));
            yellowPieces.add(new Piece(YELLOW, new Vector(5, 1), PieceType.KING));
            brownPieces.add(new Piece(BROWN, new Vector(5, 8), PieceType.KING));
        } catch (Exception e) {
            e.printStackTrace();
        }
        nullPiece = new NullPiece();
        currentPiece = nullPiece;
        // initializing pieces

        availableSquares = new ArrayList<>();
        squaresToEat = new ArrayList<>();

        teamOnTurn = YELLOW;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ArrayList<Piece> currentPieces;
                if (teamOnTurn == YELLOW) {
                    currentPieces = yellowPieces;
                } else if (teamOnTurn == BROWN) {
                    currentPieces = brownPieces;
                } else {
                    new Exception("Team out of bounds").printStackTrace();
                    return;
                }
                for (Piece p :
                        currentPieces) {
                    try {
                        if (Objects.requireNonNull(getVectorByPoint(e.getPoint())).equals(p.getVector())) {
                            if (showAvailable) {
                                availableSquares.clear();
                                squaresToEat.clear();
                            }
                            if (!currentPiece.getVector().equals(getVectorByPoint(e.getPoint()))) {
                                showAvailable = true;
                                System.out.println("show available is true");
                                availableSquares.addAll(getSquaresToGoAndEat(p).getFirst());
                                squaresToEat.addAll(getSquaresToGoAndEat(p).getSecond());
                                currentPiece = p;
                            } else {
                                showAvailable = false;
                                availableSquares.clear();
                                squaresToEat.clear();
                                currentPiece = nullPiece;
                                System.out.println("show available is false");
                            }
                            repaint();
                            return;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (showAvailable) {
                    try {
                        if (!getVectorByPoint(e.getPoint()).isIllegal() &&
                                availableSquares.contains(getVectorByPoint(e.getPoint())) ||
                                squaresToEat.contains(getVectorByPoint(e.getPoint()))) {
                            ArrayList<Piece> piecesToEat = new ArrayList<>();
                            for (Vector v :
                                    squaresToEat) {
                                piecesToEat.add(getPiece(v));
                            }
                            for (Piece p :
                                    piecesToEat) {
                                if (p.getVector().equals(getVectorByPoint(e.getPoint()))) {
                                    if (teamOnTurn == YELLOW) {
                                        brownPieces.remove(p);
                                        break;
                                    } else if (teamOnTurn == BROWN) {
                                        yellowPieces.remove(p);
                                        break;
                                    } else throw new Exception("Team out of range");
                                }
                            }
                            currentPiece.setVector(getVectorByPoint(e.getPoint()));
                            changeTeamTurn();
                        }
                        showAvailable = false;
                        availableSquares.clear();
                        squaresToEat.clear();
                        currentPiece = nullPiece;
                        System.out.println("show available is false");
                        repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

            }
        });

        repaint();


    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;


        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {

                if ((i + j) % 2 == 1) g2.setPaint(Color.PINK);
                else g2.setPaint(Color.DARK_GRAY);
                try {
                    g2.fill(new Rectangle2D.Double(getXCoorsOf(i), getYCoorsOf(j),
                            SQUARE_SIDE, SQUARE_SIDE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (Piece y : yellowPieces) {
            try {
                g2.drawImage(new ImageIcon(new ImageIcon("images\\white_" + y.getType().getName() + ".jpg").
                                getImage().getScaledInstance(
                        (int) (SQUARE_SIDE / 1.5), (int) (SQUARE_SIDE / 1.5), Image.SCALE_SMOOTH
                        )).
                                getImage(),
                        (int) getXCoorsOf(y.getVector().getX()),
                        (int) getYCoorsOf(y.getVector().getY()), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Piece b : brownPieces) {
            try {
                g2.drawImage(new ImageIcon(new ImageIcon("images\\black_" + b.getType().getName() + ".jpg").
                                getImage().getScaledInstance(
                        (int) (SQUARE_SIDE / 1.5), (int) (SQUARE_SIDE / 1.5), Image.SCALE_DEFAULT
                        )).
                                getImage(),
                        (int) getXCoorsOf(b.getVector().getX()),
                        (int) getYCoorsOf(b.getVector().getY()), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (showAvailable) {
            Stroke oldStroke = g2.getStroke();
            g2.setPaint(Color.GREEN);
            g2.setStroke(new BasicStroke(4));
            for (Vector v :
                    availableSquares) {
                try {
                    g2.draw(new Rectangle2D.Double(getXCoorsOf(v.getX()), getYCoorsOf(v.getY()),
                            SQUARE_SIDE, SQUARE_SIDE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            g2.setPaint(Color.RED);
            for (Vector v :
                    squaresToEat) {
                try {
                    g2.draw(new Rectangle2D.Double(getXCoorsOf(v.getX()), getYCoorsOf(v.getY()),
                            SQUARE_SIDE, SQUARE_SIDE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            g2.setStroke(oldStroke);

        }
        g2.setPaint(Color.GREEN);

    }


    private double getXCoorsOf(int x) throws Exception {
        if (x < 1 || x > 8) throw new Exception("Out of range: x is " + x);
        return (x - 1) * SQUARE_SIDE;
    }

    private int getXSquareOf(double x) {
        for (int i = 0; i < 8; i++) {
            if (x >= i * SQUARE_SIDE && x < (i + 1) * SQUARE_SIDE) {
                return i + 1;
            }
        }
        return -2;
    }

    private double getYCoorsOf(int y) throws Exception {
        if (y < 1 || y > 8) throw new Exception("Out of range: y is " + y);
        return (8 - y) * SQUARE_SIDE;
    }

    private int getYSquareOf(double y) {
        for (int i = 0; i < 8; i++) {
            if (y >= i * SQUARE_SIDE && y < (i + 1) * SQUARE_SIDE) {
                return 8 - i;
            }
        }
        return -2;
    }

    private Vector getVectorByPoint(Point point) {
        try {
            return new Vector(getXSquareOf(point.x), getYSquareOf(point.y));
        } catch (Exception e){
            return null;
        }
    }

    private Pair<ArrayList<Vector>> getSquaresToGoAndEat(Piece p) {
        ArrayList<Vector> toGo = new ArrayList<>();
        ArrayList<Vector> toEat = new ArrayList<>();
        Pair<ArrayList<Vector>> pair = new Pair<>(toGo, toEat);
        Vector v = p.getVector();
        int TEAM = p.getTeam();
        ArrayList<Piece> enemyPiece = null;
        ArrayList<Piece> friendPiece = null;
        if (TEAM == BROWN) {
            enemyPiece = yellowPieces;
            friendPiece = brownPieces;
        } else if (TEAM == YELLOW) {
            enemyPiece = brownPieces;
            friendPiece = yellowPieces;
        } else new Exception("Team out of range").printStackTrace();

        try {//for pawns
            if (p.getTeam() == YELLOW) {
                if (p.getType().compareTo(PieceType.PAWN) == 0) {
                    if (v.getX() != 1) {
                        if (getPiece(new Vector(v.getX() - 1, v.getY() + 1)).getTeam() == BROWN) {
                            toEat.add(new Vector(v.getX() - 1, v.getY() + 1));
                        }
                    }
                    if (v.getX() != 8) {
                        if (getPiece(new Vector(v.getX() + 1, v.getY() + 1)).getTeam() == BROWN) {
                            toEat.add(new Vector(v.getX() + 1, v.getY() + 1));
                        }
                    }
                    Vector first = new Vector(v.getX(), v.getY() + 1);
                    if (getPiece(first) instanceof NullPiece) toGo.add(first);
                    else return pair;

                    Vector second = new Vector(v.getX(), v.getY() + 2);
                    if (getPiece(second) instanceof NullPiece && v.getY() == 2) toGo.add(second);

                }
            } else if (p.getTeam() == BROWN) {
                if (p.getType().compareTo(PieceType.PAWN) == 0) {
                    if (v.getX() != 1) {
                        if (getPiece(new Vector(v.getX() - 1, v.getY() - 1)).getTeam() == YELLOW) {
                            toEat.add(new Vector(v.getX() - 1, v.getY() - 1));
                        }
                    }
                    if (v.getX() != 8) {
                        if (getPiece(new Vector(v.getX() + 1, v.getY() - 1)).getTeam() == YELLOW) {
                            toEat.add(new Vector(v.getX() + 1, v.getY() - 1));
                        }
                    }
                    Vector first = new Vector(v.getX(), v.getY() - 1);
                    if (getPiece(first) instanceof NullPiece) toGo.add(first);
                    else return pair;
                    Vector second = new Vector(v.getX(), v.getY() - 2);
                    if (getPiece(second) instanceof NullPiece && v.getY() == 7) toGo.add(second);

                }
            } else {
                throw new Exception("Team is out of range");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } //for pawns

        if (p.getType().compareTo(PieceType.KNIGHT) == 0) {
            Vector vector;
            int count = 0;
            int dx = 0;
            int dy = 0;
            while (count < 8) {
                switch (count) {
                    case 0:
                        dx = 1;
                        dy = 2;
                        break;
                    case 1:
                        dx = 1;
                        dy = -2;
                        break;
                    case 2:
                        dx = 2;
                        dy = 1;
                        break;
                    case 3:
                        dx = 2;
                        dy = -1;
                        break;
                    case 4:
                        dx = -1;
                        dy = 2;
                        break;
                    case 5:
                        dx = -1;
                        dy = -2;
                        break;
                    case 6:
                        dx = -2;
                        dy = 1;
                        break;
                    case 7:
                        dx = -2;
                        dy = -1;
                        break;
                }
                try {
                    boolean friend = false;
                    boolean enemy = false;
                    vector = new Vector(v.getX() + dx, v.getY() + dy);
                    for (Piece piece :
                            Objects.requireNonNull(friendPiece)) {
                        if (piece.getVector().equals(vector)) {
                            friend = true;
                        }
                    }
                    if (!friend) {
                        for (Piece piece : Objects.requireNonNull(enemyPiece)) {
                            if (piece.getVector().equals(vector)) {
                                toEat.add(piece.getVector());
                                enemy = true;
                            }
                        }
                        if (!enemy) toGo.add(vector);
                    }
                } catch (Exception ignored) {
                }
                count++;
            }




            /*toGo.add(new Vector(v.getX() + 1, v.getY() + 2));
            toGo.add(new Vector(v.getX() - 1, v.getY() + 2));
            toGo.add(new Vector(v.getX() + 1, v.getY() - 2));
            toGo.add(new Vector(v.getX() - 1, v.getY() - 2));
            toGo.add(new Vector(v.getX() + 2, v.getY() + 1));
            toGo.add(new Vector(v.getX() - 2, v.getY() + 1));
            toGo.add(new Vector(v.getX() + 2, v.getY() - 1));
            toGo.add(new Vector(v.getX() - 2, v.getY() - 1));*/
        } //for knights

        if (p.getType().compareTo(PieceType.KING) == 0) { //for kings
            Vector vector;
            int count = 0;
            int dx = 0;
            int dy = 0;
            while (count < 8) {
                switch (count) {
                    case 0:
                        dx = 1;
                        dy = 0;
                        break;
                    case 1:
                        dx = 1;
                        dy = 1;
                        break;
                    case 2:
                        dx = 0;
                        dy = 1;
                        break;
                    case 3:
                        dx = -1;
                        dy = 1;
                        break;
                    case 4:
                        dx = -1;
                        dy = 0;
                        break;
                    case 5:
                        dx = -1;
                        dy = -1;
                        break;
                    case 6:
                        dx = 0;
                        dy = -1;
                        break;
                    case 7:
                        dx = 1;
                        dy = -1;
                        break;
                }
                try {
                    boolean friend = false;
                    boolean enemy = false;
                    vector = new Vector(v.getX() + dx, v.getY() + dy);
                    for (Piece piece :
                            Objects.requireNonNull(friendPiece)) {
                        if (piece.getVector().equals(vector)) {
                            friend = true;
                        }
                    }
                    if (!friend) {
                        for (Piece piece : Objects.requireNonNull(enemyPiece)) {
                            if (piece.getVector().equals(vector)) {
                                toEat.add(piece.getVector());
                                enemy = true;
                            }
                        }
                        if (!enemy) toGo.add(vector);
                    }
                } catch (Exception ignored) {
                }
                count++;
            }




            /*toGo.add(new Vector(v.getX() + 1, v.getY() + 2));
            toGo.add(new Vector(v.getX() - 1, v.getY() + 2));
            toGo.add(new Vector(v.getX() + 1, v.getY() - 2));
            toGo.add(new Vector(v.getX() - 1, v.getY() - 2));
            toGo.add(new Vector(v.getX() + 2, v.getY() + 1));
            toGo.add(new Vector(v.getX() - 2, v.getY() + 1));
            toGo.add(new Vector(v.getX() + 2, v.getY() - 1));
            toGo.add(new Vector(v.getX() - 2, v.getY() - 1));*/
        } //for kings

        if (p.getType().compareTo(PieceType.BISHOP) == 0||
                p.getType().compareTo(PieceType.ROOK)==0||
                p.getType().compareTo(PieceType.QUEEN)==0) {
            Vector vector = null;

            //int BISHOP = 2;
            //int ROOK = 3;
            //int QUEEN = 6;
            PieceType type = p.getType();
            boolean queen = false;
            if(p.getType().compareTo(PieceType.QUEEN)==0){
                queen = true;
                type = PieceType.BISHOP;
            }

            try {
                vector = new Vector(p.getVector().getX(),p.getVector().getY());
            } catch (Exception e) {
                e.printStackTrace();
            }
            int dx = 0;
            int dy = 0;

            for (int i = 1; i <= 4; i++) {
                vector.setX(p.getVector().getX());
                vector.setY(p.getVector().getY());

                if(type.compareTo(PieceType.BISHOP)==0) {
                    switch (i) {
                        case 1:
                            dx = 1;
                            dy = 1;
                            break;
                        case 2:
                            dx = -1;
                            dy = 1;
                            break;
                        case 3:
                            dx = -1;
                            dy = -1;
                            break;
                        case 4:
                            dx = 1;
                            dy = -1;
                            break;
                    }
                } else if(type.compareTo(PieceType.ROOK)==0){
                    switch (i) {
                        case 1:
                            dx = 1;
                            dy = 0;
                            break;
                        case 2:
                            dx = 0;
                            dy = 1;
                            break;
                        case 3:
                            dx = -1;
                            dy = 0;
                            break;
                        case 4:
                            dx = 0;
                            dy = -1;
                            break;
                    }
                }

                boolean ended = false;
                while (!ended) {
                    try {
                        vector.setX(vector.getX()+dx);
                        vector.setY(vector.getY()+dy);


                        if (getPiece(vector) instanceof NullPiece) {
                            toGo.add(new Vector(vector.getX(), vector.getY() ));
                        } else if(getPiece(vector).getTeam()!=TEAM){
                            toEat.add(new Vector(vector.getX(), vector.getY()));
                            ended = true;
                        } else if(getPiece(vector).getTeam()==TEAM){
                            ended = true;
                        }

                    } catch (Exception e) {
                        ended = true;
                    }
                }

                if(queen&&i==4){
                    i=0;
                    type = PieceType.ROOK;
                    queen = false;
                }
            }
        }//for bishops, rooks, queens



        return pair;
    }

    private void changeTeamTurn() {
        if (teamOnTurn == YELLOW) teamOnTurn = BROWN;
        else if (teamOnTurn == BROWN) teamOnTurn = YELLOW;
        else new Exception("Team out of bounds").printStackTrace();
    }


    private Piece getPiece(Vector vector) {
        for (Piece yPiece : yellowPieces
        ) {
            if (yPiece.getVector().equals(vector)) return yPiece;
        }
        for (Piece bPiece :
                brownPieces) {
            if (bPiece.getVector().equals(vector)) return bPiece;
        }

        return nullPiece;
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }
}
