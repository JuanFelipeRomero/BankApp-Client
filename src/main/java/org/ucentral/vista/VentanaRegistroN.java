package org.ucentral.vista;

import javax.swing.*;
import java.awt.event.ActionListener;

public class VentanaRegistroN extends JFrame {
    private JButton botonRegistrar;
    private JPasswordField campoContrasena;
    private JTextField campoNombre;
    private JTextField campoCedula;
    private JTextField campoCorreo;
    private JLabel etiquetaNombre;
    private JLabel etiquetaCedula;
    private JLabel etiquetaCorreo;
    private JLabel etiquetaContrasena;
    private JPanel contentPane;

    public VentanaRegistroN() {
        // Asegurar que la UI generada por IntelliJ se usa correctamente
        setContentPane(contentPane);
        setTitle("Registrar Cliente");
        setSize(350, 380);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    // Getters para obtener los valores de los campos
    public String getNombre() {
        return campoNombre.getText();
    }

    public String getCedula() {
        return campoCedula.getText();
    }

    public String getCorreo() {
        return campoCorreo.getText();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword()); // Obtener la contraseña como String
    }

    public JButton getBotonRegistrar() { // Agregar este método
        return botonRegistrar;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }


    //Metodo para agregar ActionListener al botón-----------------
    public void agregarActionListener(ActionListener listener) {
        botonRegistrar.addActionListener(listener);
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

}
