package org.ucentral.configLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties propiedades = new Properties();

    static {
        try {
            //Cargar archivo de propiedades
            FileInputStream fis = new FileInputStream("config.client.properties");
            propiedades.load(fis);
            fis.close();
        } catch (IOException e) {
            System.err.println("Error al cargar archivo de propiedades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Metodos para obtener los valores de configuaracion
    public static int getCantidadIntentos() {
        return Integer.parseInt(propiedades.getProperty("cantidad_intentos"));
    }

    public static int getIntervaloIntentos() {
        return Integer.parseInt(propiedades.getProperty("intervalo_intentos"));
    }

    public static String getHost() {
        return propiedades.getProperty("HOST");
    }

    public static int getPort() {
        return Integer.parseInt(propiedades.getProperty("PORT"));
    }

}
