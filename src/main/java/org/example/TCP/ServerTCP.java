package org.example.TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerTCP {

    private static final int PUERTO = 55555;
    private static Set<String> usuariosConectados = new HashSet<>();

    public static void main(String[] args) {


        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket cliente = serverSocket.accept();
                new ConexionClienteTCP(cliente, usuariosConectados).start();
                //System.out.println("Usuario conectado");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
