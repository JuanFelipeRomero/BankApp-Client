package org.ucentral.comunicacionServidor;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ComunicadorServidor {
    private final int PORT = 12345; // Puerto del servidor
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean servidorActivo = false;
    private static ComunicadorServidor instancia;

    private ComunicadorServidor() {
        // Constructor privado para Singleton
    }

    public static ComunicadorServidor getInstance() {
        if (instancia == null) {
            instancia = new ComunicadorServidor();
        }
        return instancia;
    }

    public void conectar() {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket("localhost", PORT); // Conexión al servidor
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                servidorActivo = true;

                // Iniciar hilo para recibir mensajes del servidor
                new Thread(() -> {
                    String fromServer;
                    try {
                        while ((fromServer = in.readLine())!= null) {
                            // Procesar mensaje del servidor (e.g., actualizar la vista)
                            //...
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Servidor desconectado");
                        servidorActivo = false;
                        // Actualizar la vista para indicar que el servidor se ha desconectado
                        //...
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor");
            servidorActivo = false;
        }

        // Actualizar la vista según el estado de la conexión
        //...
    }

    public void enviarSolicitud(String solicitud) {
        if (out!= null && servidorActivo) {
            out.println(solicitud);
        }
    }

    public String recibirRespuesta() {
        // Implementar la lógica para recibir la respuesta del servidor
        //...
        return null;
    }

}
