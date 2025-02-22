package org.ucentral.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaOpcionesConsulta extends JFrame {

    private JButton botonConsultarConCedula;
    private JButton botonConsultarConCuenta;

    public VentanaOpcionesConsulta() {
        setTitle("Opciones de Consulta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Crear botones
        botonConsultarConCedula = new JButton("Consultar saldo con Cedula");
        botonConsultarConCuenta = new JButton("Consultar saldo con número de cuenta");

        // Crear panel y agregar botones
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(botonConsultarConCedula);
        panel.add(botonConsultarConCuenta);

        // Agregar panel al frame
        add(panel);

        setVisible(true);
    }

    // Getters para los botones
    public JButton getBotonConsultarConCedula() {
        return botonConsultarConCedula;
    }

    public JButton getBotonConsultarConCuenta() {
        return botonConsultarConCuenta;
    }

    // Método para agregar ActionListener a los botones
    public void agregarActionListener(ActionListener listener) {
        botonConsultarConCedula.addActionListener(listener);
        botonConsultarConCuenta.addActionListener(listener);
    }
}