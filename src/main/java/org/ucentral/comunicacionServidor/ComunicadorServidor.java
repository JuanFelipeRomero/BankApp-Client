package org.ucentral.comunicacionServidor;

import org.ucentral.configLoader.ConfigLoader;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ComunicadorServidor {

    private final int PORT = ConfigLoader.getPort(); // Puerto del servidor
    private final String HOST = ConfigLoader.getHost();

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

    public boolean isServidorActivo() {
        return servidorActivo;
    }

    public void conectar() {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(HOST, PORT); // Conexión al servidor
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                servidorActivo = true;
                System.out.println("Conexión al servidor establecida.");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor");
            servidorActivo = false;
            System.err.println("No se pudo conectar al servidor.");
        }
    }

    public void desconectar() {
        try {
            servidorActivo = false;
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();

            in = null;
            out = null;
            socket = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconectar() {
        desconectar(); // Primero limpiamos la conexión anterior
        conectar();    // Luego intentamos una nueva conexión
    }

    //Enviar ping para verificar la conexion
    public boolean enviarPing() {
        if (servidorActivo && out != null && in != null) {
            try {
                String mensajePing = "{\"tipoOperacion\":\"ping\"}";
                out.println(mensajePing);
                System.out.println("Ping enviado al servidor: " + mensajePing);

                String respuesta = in.readLine();
                if (respuesta != null && respuesta.contains("pong")) {
                    System.out.println("Respuesta de ping recibida: " + respuesta);
                    return true;
                } else {
                    System.err.println("No se recibió una respuesta de ping válida.");
                }
            } catch (IOException e) {
                System.err.println("Error al enviar/recibir el ping: " + e.getMessage());
                servidorActivo = false;

                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    public String enviarSolicitud(String solicitud) {
        if (!servidorActivo) {
            reconectar();
            if (!servidorActivo) return null;
        }

        if (out != null) {
            out.println(solicitud);
            System.out.println("Solicitud enviada al servidor: " + solicitud);
            String respuesta = recibirRespuesta();

            // Si la respuesta es null, podría intentar reconectar y reintentar
            if (respuesta == null && !socket.isClosed()) {
                reconectar();
                if (servidorActivo) {
                    out.println(solicitud);
                    respuesta = recibirRespuesta();
                }
            }

            return respuesta;
        }
        return null;
    }

    public String recibirRespuesta() {
        try {
            if (in != null && servidorActivo) {
                String respuesta = in.readLine();
                if (respuesta == null) {
                    System.err.println("Servidor cerró la conexión inesperadamente.");
                    servidorActivo = false; // Marcar como inactivo
                    return null;
                }
                System.out.println("Respuesta recibida del servidor: " + respuesta);
                return respuesta;
            }
        } catch (java.net.SocketException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            servidorActivo = false; // Marcar como inactivo
            // JOptionPane.showMessageDialog(null, "Servidor desconectado");
        } catch (IOException e) {
            e.printStackTrace();
            // JOptionPane.showMessageDialog(null, "Error en la respuesta del servidor: " + e.getMessage());
            servidorActivo = false;
        }
        return null;
    }
}
