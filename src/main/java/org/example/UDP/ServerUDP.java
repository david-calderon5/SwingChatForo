package org.example.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServerUDP {
    private static final int PUERTO = 5555;
    private static Map<String, InetSocketAddress> nombreUsuariosYDirecciones = new HashMap<>();

    public static void main(String[] args) {
        try (DatagramSocket datagramSocket = new DatagramSocket(PUERTO)) {
            System.out.println("Servidor de chat UDP iniciado...");

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);

                String mensajeNombreUsuario = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                InetSocketAddress inetSocketAddress = new InetSocketAddress(datagramPacket.getAddress(), datagramPacket.getPort());

                if (!nombreUsuariosYDirecciones.containsKey(mensajeNombreUsuario)) {
                    nombreUsuariosYDirecciones.put(mensajeNombreUsuario, inetSocketAddress);
                    ConexionClienteUDP hiloCliente = new ConexionClienteUDP(mensajeNombreUsuario, inetSocketAddress, nombreUsuariosYDirecciones, datagramSocket);
                    hiloCliente.run();
                    System.out.println("Se ha conectado el usuario: " + mensajeNombreUsuario);
                } else {
                    enviarMensaje("NOMBRE YA SELECCIONADO", inetSocketAddress, datagramSocket);
                }
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
}
