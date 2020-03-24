package com.daniel;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public static final EuclidPlane EUCLID_PLANE = new EuclidPlane();
    public MyFrame(){
        setSize(800,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        var jPanel = new JPanel();
        jPanel.add(EUCLID_PLANE);
        add(jPanel);
        jPanel.setBackground(Color.BLACK);

    }

    private void init() {

    }
}
