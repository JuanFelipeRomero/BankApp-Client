package org.ucentral.comunicacionServidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ComunicadorServidor {

    private final int BALANCEADOR_PORT = 6000;
    private final String BALANCEADOR_HOST = "localhost";

    private String servidorHost;
    private int servidorPort;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean servidorActivo = false;
    private static ComunicadorServidor instancia;

    private ComunicadorServidor() {}

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
        if (servidorHost == null || servidorPort == 0) {
            obtenerServidorDesdeBalanceador();
        }
        establecerConexion(servidorHost, servidorPort);
    }

    private boolean obtenerServidorDesdeBalanceador() {
        try (Socket socket = new Socket(BALANCEADOR_HOST, BALANCEADOR_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("solicitarServidor"); // Envía solicitud al balanceador
            String respuesta = in.readLine();

            if (respuesta == null || respuesta.startsWith("ERROR")) { // Verifica si la respuesta es válida
                System.err.println("Balanceador no pudo asignar un servidor: " + respuesta);
                return false;
            }

            // Separar IP y puerto correctamente
            String[] partes = respuesta.split(":");
            if (partes.length != 2) {
                System.err.println("Formato de respuesta inválido del balanceador: " + respuesta);
                return false;
            }

            servidorHost = partes[0];
            try {
                servidorPort = Integer.parseInt(partes[1]); // Convertir puerto a entero
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir el puerto: " + partes[1]);
                return false;
            }

            System.out.println("Servidor asignado: " + servidorHost + ":" + servidorPort);
            return true;

        } catch (IOException e) {
            System.err.println("Error al comunicarse con el balanceador: " + e.getMessage());
            return false;
        }
    }


    private void establecerConexion(String host, int port) {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(host, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                servidorActivo = true;
                System.out.println("Conexión establecida con el servidor: " + host + ":" + port);
            }
        } catch (IOException e) {
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
        desconectar();
        obtenerServidorDesdeBalanceador();
        establecerConexion(servidorHost, servidorPort);
    }

    public boolean enviarPing() {
        if (servidorActivo && out != null && in != null) {
            try {
                out.println("{\"tipoOperacion\":\"ping\"}");
                String respuesta = in.readLine();
                return respuesta != null && respuesta.contains("pong");
            } catch (IOException e) {
                servidorActivo = false;
            }
        }
        return false;
    }

    public String enviarSolicitud(String solicitud) {
        if (!servidorActivo) {
            conectar();
            if (!servidorActivo) return null;
        }

        if (out != null) {
            out.println(solicitud);
            String respuesta = recibirRespuesta();
            if (respuesta == null) {
                servidorActivo = false;
                reconectar();
                if (servidorActivo) {
                    out.println(solicitud);
                    return recibirRespuesta();
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
                    servidorActivo = false;
                    return null;
                }
                return respuesta;
            }
        } catch (IOException e) {
            servidorActivo = false;
        }
        return null;
    }
}