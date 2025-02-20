package org.ucentral.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    private JButton botonRegistrarse;
    private JButton botonYaSoyUsuario;

    public VentanaPrincipal() {
        setTitle("Aplicación Bancaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Crear botones
        botonRegistrarse = new JButton("Registrarse");
        botonYaSoyUsuario = new JButton("Ya soy usuario");

        // Crear panel y agregar botones
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(botonRegistrarse);
        panel.add(botonYaSoyUsuario);

        // Agregar panel al frame
        add(panel);

        setVisible(true);
    }

    // Getters para los botones
    public JButton getBotonRegistrarse() {
        return botonRegistrarse;
    }

    public JButton getBotonYaSoyUsuario() {
        return botonYaSoyUsuario;
    }

    // Método para agregar ActionListener a los botones
    public void agregarActionListener(ActionListener listener) {
        botonRegistrarse.addActionListener(listener);
        botonYaSoyUsuario.addActionListener(listener);
    }
}