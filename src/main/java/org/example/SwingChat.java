package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingChat {
    private JPanel mainPanel;
    private JTextField textFieldNombreUsuario;
    private JButton buttonAcceder;

    public SwingChat() {
        buttonAcceder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SwingChat");
        frame.setContentPane(new SwingChat().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(700, 400);
        frame.setVisible(true);
    }
}
