package org.example.UDP;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClienteUDP {
    private static final String DIRECCION_SERVIDOR = "localhost";
    private static final int PUERTO = 5555;

    private DatagramSocket datagramSocket;
    private String nombreUsuario;
    private JTextArea jTextAreaChat;
    private JTextField jTextFieldMensaje;
    private JButton jButtonEnviar;

    public ClienteUDP() {
        configurarSwing();
        intentarConexionUDP();
        iniciarEscucha();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteUDP::new);
    }

    private void configurarSwing() {
        JFrame jFramePrincipal = new JFrame("Chat UDP");
        jFramePrincipal.setSize(720, 480);
        jFramePrincipal.setLocationRelativeTo(null);
        // Para que la aplicación se detenga al cerrar la ventana
        jFramePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jTextAreaChat = new JTextArea();
        jTextAreaChat.setEditable(false);
        jFramePrincipal.add(new JScrollPane(jTextAreaChat), BorderLayout.CENTER);

        JPanel jFramePanelInferior = new JPanel(new BorderLayout());
        jTextFieldMensaje = new JTextField();
        jFramePanelInferior.add(jTextFieldMensaje, BorderLayout.CENTER);

        jButtonEnviar = new JButton("Enviar");
        jButtonEnviar.addActionListener(e -> enviarMensajeAServidor(jTextFieldMensaje.getText()));
        jFramePanelInferior.add(jButtonEnviar, BorderLayout.EAST);

        jFramePrincipal.add(jFramePanelInferior, BorderLayout.SOUTH);
        jFramePrincipal.setVisible(true);
    }


    private void intentarConexionUDP() {
        try {
            datagramSocket = new DatagramSocket();
            boolean salir = false;
            while (!salir) {
                nombreUsuario = JOptionPane.showInputDialog("Escribe tu nombre de usuario:");
                if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No has escrito nada...");
                } else {
                    enviarMensajeAServidor(nombreUsuario);
                    String mensajeServidor;
                    try {
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        datagramSocket.receive(packet);
                        mensajeServidor = new String(packet.getData(), 0, packet.getLength());
                    } catch (IOException e) {
                        mensajeServidor = "";
                    }
                    if (mensajeServidor.equals("NOMBRE YA SELECCIONADO")) {
                        JOptionPane.showMessageDialog(null, "El nombre ya está escogido. Elige otro.");
                    } else {
                        jTextAreaChat.append(mensajeServidor + "\n");
                        salir = true;
                    }
                }

            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private void enviarMensajeAServidor(String mensaje) {
        try {
            byte[] buffer = mensaje.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(DIRECCION_SERVIDOR), PUERTO);
            datagramSocket.send(packet);
            jTextFieldMensaje.setText("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void iniciarEscucha() {
        Thread escuchar = new Thread(() -> {
            while (true) {
                String mensaje;
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    datagramSocket.receive(packet);
                    mensaje = new String(packet.getData(), 0, packet.getLength());
                } catch (IOException e) {
                    mensaje = "";
                }
                jTextAreaChat.append(mensaje + "\n");
            }
        });
        escuchar.start();
    }
}
