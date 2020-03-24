package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class PlayFrame extends JFrame {
    private ShowBoard showBoard;
    private boolean started = false;
    private boolean startedFirst = false;
    private boolean win = false;
    private Board board;

    private JComboBox<String> boards;

    private JButton shuffleButton;
    private  JCheckBox compCheck;

    private JTextArea numOfShuffles;
    PlayFrame() {
        init();
    }

    private void init() {

        JMenuBar menuBar = new JMenuBar();

        JMenu editMenu = new JMenu("Edit");
        editMenu.add("Exit").addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        JPanel playPanel = new JPanel();
        board = new Board(this);
        playPanel.add(board);
        add(playPanel);
        shuffleButton = new JButton("Start");
        numOfShuffles = new JTextArea(1, 5);
        JPanel top = new JPanel();
        top.add(shuffleButton);
        top.add(numOfShuffles);

        boards = new JComboBox<>();
        boards.addItem("5x5");
        boards.addItem("9x9");
        boards.addItem("13x13");
        boards.addItem("17x17");
        boards.addItem("21x21");
        boards.addItem("25x25");

        top.add(boards);

        shuffleButton.addActionListener(new RestartAction());

        add(top,BorderLayout.NORTH);


        showBoard = new ShowBoard(board);


        add(showBoard,BorderLayout.EAST);

         compCheck = new JCheckBox("Compare");
        compCheck.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBoard.changeCompare();
            }
        });


        add(compCheck, BorderLayout.SOUTH);

        InputMap inputMap = playPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        KeyStroke downButton = KeyStroke.getKeyStroke("DOWN");
        inputMap.put(downButton, "down.action");

        KeyStroke upButton = KeyStroke.getKeyStroke("UP");
        inputMap.put(upButton, "up.action");

        KeyStroke leftButton = KeyStroke.getKeyStroke("LEFT");
        inputMap.put(leftButton, "left.action");

        KeyStroke rightButton = KeyStroke.getKeyStroke("RIGHT");
        inputMap.put(rightButton, "right.action");


        ActionMap aMap = playPanel.getActionMap();
        aMap.put("down.action", board.getDownAction());
        aMap.put("up.action", board.getUpAction());
        aMap.put("left.action", board.getLeftAction());
        aMap.put("right.action", board.getRightAction());

        pack();

    }

    ShowBoard getShowBoard() {
        return showBoard;
    }

    boolean getStarted(){
        return started;
    }

    private void setWin(){
        if(!win){
            board.showWin(true);
            win = true;
        } else{
            board.showWin(false);
            win = false;
        }
    }

    void setWin(boolean b){
        board.showWin(b);
        win = b;
    }

    public boolean getWin() {
        return win;
    }

    public boolean isStartedFirst() {
        return startedFirst;
    }

    private class RestartAction extends AbstractAction{


        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                int numOfShuffle = Integer.parseInt(numOfShuffles.getText());
                if(numOfShuffle<=1){
                    return;
                }

                shuffleButton.setText("Restart");

                int numOfRects;
                if(boards.getSelectedIndex()<2){
                    numOfRects = Integer.parseInt(String.valueOf(
                            (boards.getItemAt(boards.getSelectedIndex()).charAt(0))));
                } else{
                    numOfRects = Integer.parseInt(String.valueOf(
                            (boards.getItemAt(boards.getSelectedIndex()).charAt(0)))+
                            boards.getItemAt(boards.getSelectedIndex()).charAt(1)
                    );
                }

                System.out.println(""+ numOfRects);

                compCheck.setSelected(false);

                if(win) {
                    setWin();
                }


                    if (!started && numOfRects == 5) {
                        startedFirst = true;
                        board.restartBoard(numOfRects, numOfRects, numOfShuffle, true);
                        started = true;

                    } else {
                        board.restartBoard(numOfRects, numOfRects, numOfShuffle, false);
                        started = true;
                        startedFirst = false;
                    }



            }
            catch (Exception ignored) {

            }

        }
    }
}
