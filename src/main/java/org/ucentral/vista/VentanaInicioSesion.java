package org.ucentral.vista;

import org.ucentral.comunicacionServidor.ComunicadorServidor;

import javax.swing.*;
import java.awt.event.ActionListener;

public class VentanaInicioSesion extends JFrame {
    private JTextField campoCorreo;
    private JPasswordField campoContrasena;
    private JButton botonIniciarSesion;
    private JLabel etiquetaCorreo;
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

    public String getCorreo() {
        return campoCorreo.getText();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword()); // Obtener la contraseña como String
    }

    public void reiniciarCampos () {
        this.campoCorreo.setText("");
        this.campoContrasena.setText("");
    }

    public JButton getBotonIniciarSesion() {
        return botonIniciarSesion;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //ActionListeners
    public void agregarActionListener(ActionListener listener) {
        botonIniciarSesion.addActionListener(listener);
    }


}
