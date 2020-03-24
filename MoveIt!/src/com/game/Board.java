package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Board extends JComponent {
    private Color[][] defSquareColors;
    private boolean greenUp = true;
    private boolean blueUp = true;
    private boolean redUp = true;
    private boolean win = false;
    private Rectangle2D.Double boardBg;
    private int boardLeftPadding = 0;
    private int boardTopPadding = 0;
    private int BOARD_SIDE = 600;
    private int NUM_OF_COLUMNS = 5;
    private int NUM_OF_ROWS = 5;
    private double horLength;
    private double vertLength;
    private int[][] rectsNums;
    private int freeRect;
    private boolean[][] rectIsFilled;
    private ArrayList<Rectangle2D.Double> rects;
    private ArrayList<Piece> pieces;
    private Random random;
    private PlayFrame contentFrame;
    private ArrowAction downAction;
    private ArrowAction upAction;
    private ArrowAction leftAction;
    private ArrowAction rightAction;

    Board(PlayFrame contentFrame) {
        this.contentFrame = contentFrame;
        init();
    }


    private void init() {
        random = new Random();
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        defSquareColors = new Color[NUM_OF_ROWS][NUM_OF_COLUMNS];
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < NUM_OF_COLUMNS; j++) {
                    if (redUp) {
                        red += 10;
                        if (red >= 256) {
                            red = 255;
                            redUp = false;
                        }
                    } else {
                        red -= 10;
                        if (red <= -1) {
                            red = 0;
                            redUp = true;
                        }
                    }

                    if (greenUp) {
                        green += 15;
                        if (green >= 256) {
                            green = 255;
                            greenUp = false;
                        }
                    } else {
                        green -= 15;
                        if (green <= -1) {
                            green = 0;
                            greenUp = true;
                        }
                    }

                    if (blueUp) {
                        blue += 25;
                        if (blue >= 256) {
                            blue = 255;
                            blueUp = false;
                        }
                    } else {
                        blue -= 25;
                        if (blue <= -1) {
                            blue = 0;
                            blueUp = true;
                        }
                    }

                    defSquareColors[i][j] = new Color(red, green, blue);

                    if (j + i * NUM_OF_COLUMNS == (NUM_OF_COLUMNS * NUM_OF_ROWS - 1) / 2) {
                        defSquareColors[i][j] = new Color(255, 255, 255);
                    } else {
                        defSquareColors[i][j] = new Color(red, green, blue);
                    }

                }
            } else {
                for (int j = NUM_OF_COLUMNS - 1; j >= 0; j--) {
                    if (redUp) {
                        red += 10;
                        if (red >= 256) {
                            red = 255;
                            redUp = false;
                        }
                    } else {
                        red -= 10;
                        if (red <= -1) {
                            red = 0;
                            redUp = true;
                        }
                    }

                    if (greenUp) {
                        green += 15;
                        if (green >= 256) {
                            green = 255;
                            greenUp = false;
                        }
                    } else {
                        green -= 15;
                        if (green <= -1) {
                            green = 0;
                            greenUp = true;
                        }
                    }

                    if (blueUp) {
                        blue += 25;
                        if (blue >= 256) {
                            blue = 255;
                            blueUp = false;
                        }
                    } else {
                        blue -= 25;
                        if (blue <= -1) {
                            blue = 0;
                            blueUp = true;
                        }
                    }

                    defSquareColors[i][j] = new Color(red, green, blue);

                    if (j + i * NUM_OF_COLUMNS == (NUM_OF_COLUMNS * NUM_OF_ROWS - 1) / 2) {
                        defSquareColors[i][j] = new Color(255, 255, 255);
                    } else {
                        defSquareColors[i][j] = new Color(red, green, blue);
                    }
                }
            }
        }

        boardBg = new Rectangle2D.Double(boardLeftPadding, boardTopPadding,
                BOARD_SIDE, BOARD_SIDE);
        horLength = BOARD_SIDE / NUM_OF_COLUMNS;
        vertLength = BOARD_SIDE / NUM_OF_ROWS;
        rectsNums = new int[NUM_OF_ROWS][NUM_OF_COLUMNS];
        rectIsFilled = new boolean[NUM_OF_ROWS][NUM_OF_COLUMNS];
        rects = new ArrayList<>();
        pieces = new ArrayList<>();
        int cur = 0;
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLUMNS; j++) {
                rectsNums[i][j] = cur;
                Rectangle2D.Double rect = new Rectangle2D.Double(boardLeftPadding + j * horLength,
                        boardTopPadding + i * vertLength,
                        horLength, vertLength);
                rects.add(cur, rect);
                cur++;
            }
        }

        freeRect = rectsNums[(NUM_OF_ROWS - 1) / 2][(NUM_OF_COLUMNS - 1) / 2];


        makePieces();

        repaint();


    }

    ArrowAction getUpAction() {
        return upAction;
    }

    ArrowAction getDownAction() {
        return downAction;
    }

    ArrowAction getLeftAction() {
        return leftAction;
    }

    ArrowAction getRightAction() {
        return rightAction;
    }

    private void makePieces() {
        downAction = new ArrowAction(Vector.DOWN);

        upAction = new ArrowAction(Vector.UP);

        leftAction = new ArrowAction(Vector.LEFT);

        rightAction = new ArrowAction(Vector.RIGHT);

        int max = NUM_OF_ROWS * NUM_OF_COLUMNS - 1;
        int filled = 0;

        while (filled < max) {
            for (int y = 0; y < NUM_OF_COLUMNS; y++) {
                for (int x = 0; x < NUM_OF_ROWS; x++) {
                    if (!rectIsFilled[y][x] && rectsNums[y][x] != freeRect) {
                        int length;
                        int orientation;

                        // 1 1 1 1 2
                        // 1 1 1 1 2
                        // 1 1 1 1 2
                        // 1 1 1 1 2
                        // 3 3 3 3 4

                        if (y < NUM_OF_ROWS - 1) {

                            if (x < NUM_OF_COLUMNS - 1) { //1

                                if (!rectIsFilled[y][x + 1]) {


                                    length = random.nextInt(2) + 1;

                                    if (length == 2) {
                                        orientation = random.nextInt(2) + 1;
                                    } else {
                                        orientation = Piece.NO_ORIENTATION;
                                    }

                                } else {
                                    length = random.nextInt(2) + 1;
                                    if (length == 2) {
                                        orientation = Piece.VERTICAL;
                                    } else {
                                        orientation = Piece.NO_ORIENTATION;
                                    }
                                }

                            } else { //2

                                length = random.nextInt(2) + 1;
                                if (length == 2) {
                                    orientation = Piece.VERTICAL;
                                } else {
                                    orientation = Piece.NO_ORIENTATION;
                                }
                            }

                        } else {

                            if (x < NUM_OF_COLUMNS - 1) { //3

                                if (!rectIsFilled[y][x + 1]) {


                                    length = random.nextInt(2) + 1;

                                    if (length == 2) {
                                        orientation = Piece.HORIZONTAL;
                                    } else {
                                        orientation = Piece.NO_ORIENTATION;
                                    }

                                } else {
                                    length = 1;
                                    orientation = Piece.NO_ORIENTATION;

                                }

                            } else {
                                length = 1;
                                orientation = Piece.NO_ORIENTATION;
                            }

                        }


                        Piece curPiece = new Piece(x, y, length, orientation,
                                defSquareColors[y][x], this);

                        rectIsFilled[y][x] = true;
                        filled++;
                        if (length == 2) {
                            filled++;
                            if (orientation == Piece.HORIZONTAL) {
                                rectIsFilled[y][x + 1] = true;
                                curPiece = new Piece(x, y, length, orientation,
                                        defSquareColors[y][x], defSquareColors[y][x + 1], this);
                            } else if (orientation == Piece.VERTICAL) {
                                rectIsFilled[y + 1][x] = true;
                                curPiece = new Piece(x, y, length, orientation,
                                        defSquareColors[y][x], defSquareColors[y + 1][x], this);
                            }
                        } else {
                            curPiece = new Piece(x, y, length, orientation,
                                    defSquareColors[y][x], this);
                        }
                        pieces.add(curPiece);


                    }
                }
            }
        }

        Piece p = getPieceWithRectNum((NUM_OF_COLUMNS * NUM_OF_ROWS - 1) / 2);
        if (p != null) {
            p.setLength(1);
            p.setFieldRects();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.WHITE);
        g2.fill(boardBg);

        g2.setPaint(Color.GRAY);
        for (int i = 1; i < NUM_OF_ROWS; i++) {
            g2.drawLine(boardLeftPadding, (int) (boardTopPadding + vertLength * i),
                    boardLeftPadding + BOARD_SIDE, (int) (boardTopPadding + vertLength * i));
        }
        for (int i = 1; i < NUM_OF_COLUMNS; i++) {
            g2.drawLine((int) (boardLeftPadding + horLength * i), boardTopPadding,
                    (int) (boardLeftPadding + horLength * i), boardTopPadding + BOARD_SIDE);
        }


        for (Piece p :
                pieces) {
            double xStart = 0;
            double yStart = 0;
            double xEnd;
            double yEnd;
            boolean start = true;
            boolean colorChecked = false;
            boolean[][] filled = p.getFilledRects();
            for (int i = 0; i < NUM_OF_ROWS; i++) {
                for (int j = 0; j < NUM_OF_COLUMNS; j++) {

                    if (filled[i][j]) {
                        if (!colorChecked) {
                            g2.setPaint(p.getColor1());
                            colorChecked = true;
                        } else {
                            g2.setPaint(p.getColor2());
                        }
                        Rectangle2D.Double currentRectangle = getRectWithNum(rectsNums[i][j]);
                        g2.fill(currentRectangle);
                        if (p.getLength() == 1) {
                            g2.setPaint(Color.GRAY.darker());
                            g2.draw(new Rectangle2D.Double(currentRectangle.x + horLength / 4
                                    , currentRectangle.y + vertLength / 4,
                                    horLength * 0.5,
                                    vertLength * 0.5));
                        } else {
                            if (start) {
                                g2.setPaint(Color.GRAY.darker());
                                xStart = currentRectangle.x + horLength / 4;
                                yStart = currentRectangle.y + vertLength / 4;
                                start = false;
                            } else {
                                g2.setPaint(Color.GRAY.darker());
                                xEnd = currentRectangle.x + 0.75 * horLength;
                                yEnd = currentRectangle.y + 0.75 * vertLength;

                                Rectangle2D.Double r = new Rectangle2D.Double(xStart, yStart,
                                        xEnd - xStart, yEnd - yStart);
                                g2.draw(r);
                            }
                        }
                    }

                }
            }
        }


        g2.setPaint(Color.WHITE);
        g2.fill(rects.get(freeRect));

        if (!win) {
            contentFrame.getShowBoard().compare();
        }
        if (win) {
            g2.fill(new Rectangle2D.Double(200, 200, 480, 240));
            g2.setPaint(Color.BLACK);
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 36));
            g2.drawString("WINNER!", 300, 300);
        }


    }

    Rectangle2D.Double getRectWithNum(int num) {
        int xPart = num % NUM_OF_COLUMNS;
        int yPart = (num - xPart) / NUM_OF_ROWS;

        int cur = rectsNums[yPart][xPart];

        return rects.get(cur);
    }

    int[][] getRectsNums() {
        return rectsNums;
    }

    int getNUM_OF_COLUMNS() {
        return NUM_OF_COLUMNS;
    }

    int getNUM_OF_ROWS() {
        return NUM_OF_ROWS;
    }

    double getHorLength() {
        return this.horLength;
    }

    double getVertLength() {
        return this.vertLength;
    }

    private void shuffle(int number) {
        System.out.println("Shuffling...");
        for (int i = 0; i < number; i++) {
            int numOfMoves = 0;
            ArrayList<Vector> vectors = new ArrayList<>();
            ArrayList<Piece> canMovers = new ArrayList<>();
            int freeX = (int) ((rects.get(freeRect).x - boardLeftPadding) / horLength);
            int freeY = (int) ((rects.get(freeRect).y - boardTopPadding) / vertLength);


            // 5 4 4 4 4 6
            // 2 1 1 1 1 3
            // 2 1 1 1 1 3
            // 2 1 1 1 1 3
            // 2 1 1 1 1 3
            // 8 7 7 7 7 9

            if (freeY > 0) {
                if (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY - 1][freeX])).getLength() == 1 ||
                        (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY - 1][freeX])).getLength() == 2 &&
                                Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY - 1][freeX])).getOrientation() == Piece.VERTICAL)) {
                    vectors.add(Vector.DOWN);
                    canMovers.add(getPieceWithRectNum(rectsNums[freeY - 1][freeX]));
                    numOfMoves++;
                }
            }
            if (freeY < NUM_OF_ROWS - 1) {
                if (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY + 1][freeX])).getLength() == 1 ||
                        (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY + 1][freeX])).getLength() == 2 &&
                                Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY + 1][freeX])).getOrientation() == Piece.VERTICAL)) {
                    vectors.add(Vector.UP);
                    canMovers.add(getPieceWithRectNum(rectsNums[freeY + 1][freeX]));
                    numOfMoves++;
                }
            }
            if (freeX > 0) {
                if (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY][freeX - 1])).getLength() == 1
                        || (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY][freeX - 1])).getLength() == 2 &&
                        Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY][freeX - 1])).getOrientation() == Piece.HORIZONTAL)) {
                    vectors.add(Vector.RIGHT);
                    canMovers.add(getPieceWithRectNum(rectsNums[freeY][freeX - 1]));
                    numOfMoves++;
                }
            }

            if (freeX < NUM_OF_COLUMNS - 1) {
                if (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY][freeX + 1])).getLength() == 1
                        || (Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY][freeX + 1])).getLength() == 2 &&
                        Objects.requireNonNull(getPieceWithRectNum(rectsNums[freeY][freeX + 1])).getOrientation() == Piece.HORIZONTAL)) {
                    vectors.add(Vector.LEFT);
                    canMovers.add(getPieceWithRectNum(rectsNums[freeY][freeX + 1]));
                    numOfMoves++;
                }
            }


            if (numOfMoves > 0) {
                int k = random.nextInt(numOfMoves);

                Piece mover = canMovers.get(k);
                Vector v = vectors.get(k);
                if (mover.getLength() == 1) {
                    freeRect = rectsNums[mover.getY()][mover.getX()];
                    mover.setX(freeX);
                    mover.setY(freeY);
                    mover.setFieldRects();
                } else {
                    if (v.equals(Vector.UP)) {
                        freeRect = rectsNums[mover.getY() + 1][mover.getX()];
                    } else if (v.equals(Vector.DOWN)) {
                        freeRect = rectsNums[mover.getY()][mover.getX()];
                    } else if (v.equals(Vector.RIGHT)) {
                        freeRect = rectsNums[mover.getY()][mover.getX()];
                    } else if (v.equals(Vector.LEFT)) {
                        freeRect = rectsNums[mover.getY()][mover.getX() + 1];
                    }
                    mover.allRectsFalse();
                    mover.setX(mover.getX() + v.getX());
                    mover.setY(mover.getY() + v.getY());
                    mover.setFieldRects();

                }

            } else {
                System.out.println("Cant move!");
            }


        }
        if(!contentFrame.getWin()) {
            repaint();
        }
    }

    private Piece getPieceWithRectNum(int n) {
        int xPart = n % NUM_OF_COLUMNS;
        int yPart = (n - xPart) / NUM_OF_ROWS;
        if (rectsNums[yPart][xPart] == freeRect) {
            System.out.println("FREE RECT!");
        }
        for (Piece p :
                pieces) {
            if (rectsNums[yPart][xPart] == n) {
                if (p.getFilledRects()[yPart][xPart]) {

                    return p;
                }

            }

        }

        return null;
    }

    void restartBoard(int rows, int columns, int shuffle, boolean onlyShuffle) {
        if (!onlyShuffle) {
            this.NUM_OF_ROWS = rows;
            this.NUM_OF_COLUMNS = columns;
            init();
        }

        int numOfRestarts =-1;
        do {
            shuffle(shuffle);

            contentFrame.getShowBoard().restart();
            contentFrame.getShowBoard().compare();
            numOfRestarts++;
            if(numOfRestarts>10){
                restartBoard(rows,columns, shuffle, onlyShuffle);
                return;
            }
            repaint();
        }while(win);
        repaint();
    }

    Color[][] getCurColors() {
        HashMap<Piece, Boolean> map = new HashMap<>();
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLUMNS; j++) {
                Piece p = getPieceWithRectNum(rectsNums[i][j]);
                map.put(p, false);
            }
        }
        Color[][] g = new Color[NUM_OF_ROWS][NUM_OF_COLUMNS];
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLUMNS; j++) {
                if ((rectsNums[i][j] != freeRect)) {
                    Piece p = getPieceWithRectNum(rectsNums[i][j]);
                    if (!map.get(p)) {
                        g[i][j] = p.getColor1();
                        map.put(p, true);
                    } else {
                        g[i][j] = p.getColor2();
                    }
                } else {
                    g[i][j] = Color.WHITE;
                }
            }
        }
        return g;
    }

    Color[][] getDefSquareColors() {
        return defSquareColors;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    PlayFrame getContentFrame() {
        return this.contentFrame;
    }

    void showWin(boolean won) {
        win = won;
    }

    private class ArrowAction extends AbstractAction {

        ArrowAction(Vector vector) {
            putValue("vector", vector);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (contentFrame.getStarted()) {
                Vector cur = (Vector) (getValue("vector"));
                int freeX = (int) ((rects.get(freeRect).x - boardLeftPadding) / horLength);
                int freeY = (int) ((rects.get(freeRect).y - boardTopPadding) / vertLength);


                // 5 4 4 4 4 6
                // 2 1 1 1 1 3
                // 2 1 1 1 1 3
                // 2 1 1 1 1 3
                // 2 1 1 1 1 3
                // 8 7 7 7 7 9

                if (freeY > 0 && cur.equals(Vector.DOWN)) {
                    Piece mover = getPieceWithRectNum(rectsNums[freeY - 1][freeX]);
                    if (mover.getLength() == 1 ||
                            (mover.getLength() == 2 &&
                                    mover.getOrientation() == Piece.VERTICAL)) {


                        if (mover.getLength() == 1) {
                            int moverX = mover.getX();
                            int moverY = mover.getY();
                            mover.setX(freeX);
                            mover.setY(freeY);
                            mover.setFieldRects();
                            freeRect = rectsNums[moverY][moverX];
                        } else {
                            int moverX = mover.getX();
                            int moverY = mover.getY();


                            mover.allRectsFalse();
                            mover.setX(mover.getX() + cur.getX());
                            mover.setY(mover.getY() + cur.getY());
                            mover.setFieldRects();

                            freeRect = rectsNums[moverY][moverX];
                        }


                    }
                }
                if (freeY < NUM_OF_ROWS - 1 && cur.equals(Vector.UP)) {
                    Piece mover = getPieceWithRectNum(rectsNums[freeY + 1][freeX]);
                    if (mover.getLength() == 1 ||
                            (mover.getLength() == 2 &&
                                    mover.getOrientation() == Piece.VERTICAL)) {


                        if (mover.getLength() == 1) {
                            int moverX = mover.getX();
                            int moverY = mover.getY();
                            mover.setX(freeX);
                            mover.setY(freeY);
                            mover.setFieldRects();

                            freeRect = rectsNums[moverY][moverX];
                        } else {
                            int moverX = mover.getX();
                            int moverY = mover.getY();


                            mover.allRectsFalse();
                            mover.setX(mover.getX() + cur.getX());
                            mover.setY(mover.getY() + cur.getY());
                            mover.setFieldRects();

                            freeRect = rectsNums[moverY + 1][moverX];
                        }

                    }
                }
                if (freeX > 0 && cur.equals(Vector.RIGHT)) {
                    Piece mover = getPieceWithRectNum(rectsNums[freeY][freeX - 1]);
                    if (mover.getLength() == 1
                            || (mover.getLength() == 2 &&
                            mover.getOrientation() == Piece.HORIZONTAL)) {


                        int moverX = mover.getX();
                        int moverY = mover.getY();

                        if (mover.getLength() == 1) {

                            mover.setX(freeX);
                            mover.setY(freeY);
                            mover.setFieldRects();
                            freeRect = rectsNums[moverY][moverX];
                        } else {


                            mover.allRectsFalse();
                            mover.setX(mover.getX() + cur.getX());
                            mover.setY(mover.getY() + cur.getY());
                            mover.setFieldRects();
                            freeRect = rectsNums[moverY][moverX];
                        }

                    }
                }

                if (freeX < NUM_OF_COLUMNS - 1 && cur.equals(Vector.LEFT)) {
                    if (getPieceWithRectNum(rectsNums[freeY][freeX + 1]).getLength() == 1
                            || (getPieceWithRectNum(rectsNums[freeY][freeX + 1]).getLength() == 2 &&
                            getPieceWithRectNum(rectsNums[freeY][freeX + 1]).getOrientation() == Piece.HORIZONTAL)) {

                        Piece mover = getPieceWithRectNum(rectsNums[freeY][freeX + 1]);


                        int moverX = mover.getX();
                        int moverY = mover.getY();

                        if (mover.getLength() == 1) {

                            mover.setX(freeX);
                            mover.setY(freeY);
                            mover.setFieldRects();
                            freeRect = rectsNums[moverY][moverX];
                        } else {


                            mover.allRectsFalse();
                            mover.setX(mover.getX() + cur.getX());
                            mover.setY(mover.getY() + cur.getY());
                            mover.setFieldRects();
                            freeRect = rectsNums[moverY][moverX + 1];

                        }

                    }
                }
                repaint();
            }
        }
    }
}
