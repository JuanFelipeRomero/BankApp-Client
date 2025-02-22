package org.ucentral.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaRegistro extends JFrame {

    private JLabel etiquetaNombre;
    private JTextField campoNombre;
    private JLabel etiquetaCedula;
    private JTextField campoCedula;
    private JLabel etiquetaCorreo;
    private JTextField campoCorreo;
    private JLabel etiquetaContrasena;
    private JPasswordField campoContrasena;
    private JButton botonRegistrar;

    public VentanaRegistro() {
        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(null);

        // Crear componentes
        etiquetaNombre = new JLabel("Nombre:");
        campoNombre = new JTextField(20);
        etiquetaCedula = new JLabel("Cedula:");
        campoCedula = new JTextField(10);
        etiquetaCorreo = new JLabel("Correo:");
        campoCorreo = new JTextField(20);
        etiquetaContrasena = new JLabel("Contraseña:");
        campoContrasena = new JPasswordField(20);
        botonRegistrar = new JButton("Registrar");

        // Crear panel y agregar componentes
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.add(etiquetaNombre);
        panel.add(campoNombre);
        panel.add(etiquetaCedula);
        panel.add(campoCedula);
        panel.add(etiquetaCorreo);
        panel.add(campoCorreo);
        panel.add(etiquetaContrasena);
        panel.add(campoContrasena);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(botonRegistrar);

        // Agregar panel al frame
        add(panel);

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



    // Método para agregar ActionListener al botón
    public void agregarActionListener(ActionListener listener) {
        botonRegistrar.addActionListener(listener);
    }


    public void mostrarMensaje(String msg) {
        System.out.println(msg);
    }
}