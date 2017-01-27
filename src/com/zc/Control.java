package com.zc;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * Created by Josh on 1/27/2017.
 */
public class Control {
    private JButton buttonStart;
    private JPanel panelMain;
    private JButton buttonQuit;

    public Control() {
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RMSNotify rmsn = new RMSNotify();
                try {
                    rmsn.start();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Started!");
            }
        });
        buttonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    public static void main(String args[]) {
        JFrame frame = new JFrame("Control");
        frame.setContentPane(new Control().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
