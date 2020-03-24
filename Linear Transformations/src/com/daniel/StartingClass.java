package com.daniel;

import java.awt.*;

public class StartingClass {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var myFrame = new MyFrame();
        });
    }
}
