package com.dan;

import javax.swing.*;

public class MyFrame extends JFrame {
    public MyFrame() throws Exception {
        init();
    }

    private void init() throws Exception {
        JPanel panel = new JPanel();
        Chessboard chessboard = new Chessboard();
        panel.add(chessboard);
        add(panel);
    }
}
