package org.ucentral.vista;

import org.ucentral.comunicacionServidor.ComunicadorServidor;

import javax.swing.*;
import java.awt.event.ActionListener;

public class VentanaInicioSesion extends JFrame {
    private JTextField campoCedula;
    private JPasswordField campoContrasena;
    private JButton botonIniciarSesion;
    private JLabel etiquetaCedula;
    private JLabel etiquetaContrasena;
    private JPanel contentPane;

    public VentanaInicioSesion() {
        // Asegurar que la UI generada por IntelliJ se usa correctamente
        setContentPane(contentPane);
        setTitle("Iniciar Sesion");
        setSize(350, 325);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getCedula() {
        return campoCedula.getText();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword()); // Obtener la contraseña como String
    }

    public JButton getBotonIniciarSesion() {
        return botonIniciarSesion;
    }

    //ActionListeners
    public void agregarActionListener(ActionListener listener) {
        botonIniciarSesion.addActionListener(listener);
    }


}
