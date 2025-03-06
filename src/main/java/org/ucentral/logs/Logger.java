package org.ucentral.logs;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Logger {
    private static final String LOG_FILE = "logs.txt"; // Archivo donde se guardarán los logs
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String mensaje, boolean guardarEnArchivo) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = "[" + timestamp + "] " + mensaje;

        if (guardarEnArchivo) {
            escribirEnArchivo(logMessage);
        }
    }

    private static void escribirEnArchivo(String mensaje) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(mensaje);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de logs: " + e.getMessage());
        }
    }
}


