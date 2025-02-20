package org.ucentral;

import org.ucentral.controlador.Controlador;
import org.ucentral.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        VentanaPrincipal vista = new VentanaPrincipal();
        Controlador controlador = new Controlador(vista);
        System.out.println("Iniciando aplicacion...");
    }
}