package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Objects;

public class ShowBoard extends JComponent {
    private Board from;
    private Color[][] thisColors;
    private HashMap<Integer, Boolean> checks;
    private boolean compare;

    ShowBoard(Board from) {
        compare = false;
        this.from = from;
        thisColors = from.getDefSquareColors();
        checks = new HashMap<>();

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Image tick = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(
                "com/game/images/chbox.png"))).getImage();
            for (int i = 0; i < from.getNUM_OF_ROWS(); i++) {
                for (int j = 0; j < from.getNUM_OF_COLUMNS(); j++) {


                        g2.setPaint(thisColors[i][j]);
                        g2.fill(from.getRectWithNum(from.getRectsNums()[i][j]));
                        if (compare) {
                            Rectangle2D.Double rect = from.getRectWithNum(from.getRectsNums()[i][j]);
                            if (checks.get(from.getRectsNums()[i][j])) {
                                g2.drawImage(tick,
                                            (int) rect.x, (int) rect.y
                                        , (int)from.getHorLength(),
                                        (int)from.getVertLength(), null
                                );
                            }
                        }

                }
            }

    }

    void compare() {

        int numOfChecks =0;
        Color[][] leftColors = from.getCurColors();
        for (int i = 0; i < from.getNUM_OF_ROWS(); i++) {
            for (int j = 0; j < from.getNUM_OF_COLUMNS(); j++) {
                boolean ident = leftColors[i][j].equals(thisColors[i][j]);
                if(ident){
                    numOfChecks++;
                }
                int rectNum = from.getRectsNums()[i][j];
                checks.put(rectNum, ident);
            }
        }
        if (numOfChecks==(from.getNUM_OF_COLUMNS()*from.getNUM_OF_ROWS())){
            if(from.getContentFrame().getStarted()||from.getContentFrame().isStartedFirst()) {
                from.getContentFrame().setWin(true);
            }
        } else{
            from.getContentFrame().setWin(false);
        }
        repaint();
    }


    void changeCompare() {
        if (!compare) {
            compare = true;
            compare();
        } else {
            compare = false;
            repaint();
        }
    }

    void restart(){
        compare = false;
        thisColors = from.getDefSquareColors();
        checks = new HashMap<>();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }
}
