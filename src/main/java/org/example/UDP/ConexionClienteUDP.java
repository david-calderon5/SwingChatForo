package org.example.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;

public class ConexionClienteUDP implements Runnable {
    private static Map<String, InetSocketAddress> nombreUsuariosYDirecciones;
    private String nombreUsuario;
    private InetSocketAddress inetSocketAddress;
    private DatagramSocket datagramSocket;

    public ConexionClienteUDP(String nombreUsuario, InetSocketAddress inetSocketAddress, Map<String, InetSocketAddress> nombreUsuariosYDirecciones, DatagramSocket datagramSocket) throws SocketException {
        this.nombreUsuario = nombreUsuario;
        this.inetSocketAddress = inetSocketAddress;
        this.nombreUsuariosYDirecciones = nombreUsuariosYDirecciones;
        this.datagramSocket = datagramSocket;
    }

    public void run() {
        try {
            enviarMensaje("SERVIDOR: Bienvenido/a al chat común " + nombreUsuario, inetSocketAddress, datagramSocket);
            enviarATodos("SERVIDOR: " + nombreUsuario + " se ha unido al chat", datagramSocket);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);

                String mensaje = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                enviarATodos(nombreUsuario + ": " + mensaje, datagramSocket);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void enviarMensaje(String mensaje, InetSocketAddress inetSocketAddress, DatagramSocket datagramSocket) throws Exception {
        byte[] buffer = mensaje.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        datagramSocket.send(packet);
    }

    private static void enviarATodos(String mensaje, DatagramSocket datagramSocket) throws Exception {
        byte[] buffer = mensaje.getBytes();
        for (Map.Entry<String, InetSocketAddress> entry : nombreUsuariosYDirecciones.entrySet()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, entry.getValue().getAddress(), entry.getValue().getPort());
            datagramSocket.send(packet);
        }
    }
}
