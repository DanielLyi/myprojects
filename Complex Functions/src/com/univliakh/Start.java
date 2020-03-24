package com.univliakh;

import javax.swing.*;
import java.awt.*;

public class Start {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            MyFrame frame = new MyFrame();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(1600,800);

        });
    }
}
