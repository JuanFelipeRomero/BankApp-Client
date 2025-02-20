package org.ucentral.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaOpcionesConsulta extends JFrame {

    private JButton botonConsultarConId;
    private JButton botonConsultarConCuenta;

    public VentanaOpcionesConsulta() {
        setTitle("Opciones de Consulta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Crear botones
        botonConsultarConId = new JButton("Consultar saldo con ID");
        botonConsultarConCuenta = new JButton("Consultar saldo con número de cuenta");

        // Crear panel y agregar botones
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(botonConsultarConId);
        panel.add(botonConsultarConCuenta);

        // Agregar panel al frame
        add(panel);

        setVisible(true);
    }

    // Getters para los botones
    public JButton getBotonConsultarConId() {
        return botonConsultarConId;
    }

    public JButton getBotonConsultarConCuenta() {
        return botonConsultarConCuenta;
    }

    // Método para agregar ActionListener a los botones
    public void agregarActionListener(ActionListener listener) {
        botonConsultarConId.addActionListener(listener);
        botonConsultarConCuenta.addActionListener(listener);
    }
}