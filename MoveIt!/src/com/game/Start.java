package com.game;

import javax.swing.*;
import java.awt.*;

public class Start {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            PlayFrame playFrame = new PlayFrame();
            playFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            playFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            playFrame.setVisible(true);
            playFrame.setTitle("Move it");
        });
    }
}
