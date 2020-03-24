package com.univliakh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyFrame extends JFrame {

    private JLabel inputLabel;
    private JLabel outputLabel;
    private JLabel centerLabel;


    public MyFrame(){
        JPanel topPanel = new JPanel(new GridLayout(1,3));
        inputLabel = new JLabel("Input");
        outputLabel = new JLabel("Output");
        centerLabel = new JLabel("W = Z^2");
        topPanel.add(inputLabel);
        topPanel.add(centerLabel);
        topPanel.add(outputLabel);

        JPanel planePanel = new JPanel(new GridLayout(1,2));



        OutputPlane outputPlane = new OutputPlane(this,z->{
            double rP = z.getRealPart();
            double iP = z.getImaginaryPart();
            double newRP;
            double newIP;

            newRP = rP*rP-iP*iP;
            newIP = 2*rP*iP;

            return new ComplexNumber(newRP,newIP);
        });



        InputPlane inputPlane = new InputPlane(this,outputPlane);
        planePanel.add(inputPlane);
        planePanel.add(outputPlane);

        inputPlane.drawPoint(2,2,true,false);


        add(topPanel,BorderLayout.NORTH);
        add(planePanel,BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu editMenu = new JMenu("Edit");





        var refreshAction = new AbstractAction("Refresh"){

            @Override
            public void actionPerformed(ActionEvent e) {
                inputPlane.killAllPoints();
                outputPlane.killAllPoints();
            }
        };

        editMenu.add(refreshAction).setAccelerator(KeyStroke.getKeyStroke("ctrl R"));

        menuBar.add(editMenu);
        setJMenuBar(menuBar);

    }
}
