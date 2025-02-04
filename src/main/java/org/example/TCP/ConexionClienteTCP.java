package org.example.TCP;

import java.io.*;
import java.net.Socket;
import java.util.Set;

public class ConexionClienteTCP extends Thread{
    private Socket cliente;
    private Set<String> usuariosConectados;
    public ConexionClienteTCP(Socket cliente, Set<String> usuariosConectados) {
        this.cliente = cliente;
        this.usuariosConectados = usuariosConectados;
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()))) {
            boolean conectado;
            do {
                String nombreUsuario = bufferedReader.readLine();
                System.out.println(nombreUsuario);
                conectado = usuariosConectados.add(nombreUsuario);
            } while (!conectado);
            System.out.println("Usuario conectado");
            bufferedWriter.write("Conexión correcta\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
