package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingChat {
    private JPanel mainPanel;
    private static final JFrame frameBase = new JFrame("SwingChat");
    private JButton buttonAcceder;
    private JPanel primerPanel;
    private JLabel labelNombre;
    private JTextField textFieldNombreUsuario;
    private JPanel segundoPanel;
    private JButton button1;

    public SwingChat() {
        buttonAcceder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                primerPanel.setVisible(false);
                frameBase.setContentPane(new SwingChat().segundoPanel);
                segundoPanel.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {

        frameBase.setContentPane(new SwingChat().mainPanel);
        frameBase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameBase.pack();
        frameBase.setLocationRelativeTo(null);
        frameBase.setResizable(false);
        frameBase.setSize(700, 400);
        frameBase.setVisible(true);

        frameBase.setContentPane(new SwingChat().primerPanel);
    }
}
