package org.example.TCP;

import java.io.*;
import java.net.Socket;
import java.util.Set;

public class ConexionClienteTCP extends Thread{
    private Socket cliente;
    private String nombrePropio;
    private Set<String> usuariosConectados;
    private BufferedWriter bufferedWriterUnico;
    private Set<BufferedWriter> clientesConectados;
    public ConexionClienteTCP(Socket cliente, Set<String> usuariosConectados, Set<BufferedWriter> clientesConectados) {
        this.cliente = cliente;
        this.clientesConectados = clientesConectados;
        this.usuariosConectados = usuariosConectados;
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()))) {
            bufferedWriterUnico = bufferedWriter;
            boolean conectado;
            do {
                nombrePropio = bufferedReader.readLine();
                System.out.println(nombrePropio);
                synchronized (usuariosConectados) {
                    conectado = usuariosConectados.add(nombrePropio);
                    if (!conectado) {
                        bufferedWriter.write("Nombre no disponible\n");
                        bufferedWriter.flush();
                    } else {
                        System.out.println("Usuario conectado\n");
                        bufferedWriter.write("Conexión correcta\n");
                        bufferedWriter.flush();
                        clientesConectados.add(bufferedWriter);
                    }
                }
            } while (!conectado);

            String mensaje;
            while ((mensaje = bufferedReader.readLine()) != null) {
                enviarMensajeATodos(mensaje);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                cliente.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            synchronized (usuariosConectados) {
                usuariosConectados.remove(nombrePropio);
            }
            synchronized (clientesConectados) {
                clientesConectados.remove(bufferedWriterUnico);
            }
            System.out.println(nombrePropio + " se ha desconectado.");
        }
    }

    private void enviarMensajeATodos(String mensaje) {
        synchronized (clientesConectados) {
            for (BufferedWriter cliente : clientesConectados) {
                try {
                    cliente.write(nombrePropio + ": " + mensaje + "\n");
                    cliente.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
