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
                System.out.println("Conexión al servidor establecida.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor");
            servidorActivo = false;
            System.err.println("No se pudo conectar al servidor.");
        }
    }

    // Método síncrono: envía la solicitud y espera la respuesta inmediatamente.
    public String enviarSolicitud(String solicitud) {
        if (out != null && servidorActivo) {
            out.println(solicitud);
            System.out.println("Solicitud enviada al servidor: " + solicitud);
            return recibirRespuesta();
        }
        return null;
    }

    public String recibirRespuesta() {
        try {
            if (in != null && servidorActivo) {
                String respuesta = in.readLine();
                if (respuesta == null) {
                    System.err.println("Servidor cerró la conexión inesperadamente.");
                    return null;
                }
                System.out.println("Respuesta recibida del servidor: " + respuesta);
                return respuesta;
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al recibir respuesta del servidor");
            servidorActivo = false;
            System.err.println("Error al recibir respuesta del servidor: " + e);
        }
        return null;
    }
}
