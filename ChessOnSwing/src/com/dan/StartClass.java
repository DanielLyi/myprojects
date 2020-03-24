package com.dan;

import javax.swing.*;
import java.awt.*;

public class StartClass {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MyFrame frame;
            try {
                frame = new MyFrame();

                frame.setSize(800, 800);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

