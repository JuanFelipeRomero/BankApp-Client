package org.ucentral;

import org.ucentral.controlador.Controlador;
import org.ucentral.vista.VentanaPrincipalN;

public class Main {
    public static void main(String[] args) {

        VentanaPrincipalN vista = new VentanaPrincipalN();
        Controlador controlador = new Controlador(vista);
        System.out.println("Iniciando aplicacion...");
    }
}