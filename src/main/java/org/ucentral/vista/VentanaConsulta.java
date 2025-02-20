package org.ucentral.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaConsulta extends JFrame {

    private JLabel etiquetaId;
    private JTextField campoId;
    private JLabel etiquetaNumeroCuenta;
    private JTextField campoNumeroCuenta;
    private JButton botonConsultar;
    private JLabel etiquetaSaldo;
    private JTextField campoSaldo;
    private JPanel panel;

    public VentanaConsulta() {
        setTitle("Consulta de Saldo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);

        // Crear componentes
        etiquetaId = new JLabel("ID de usuario:");
        campoId = new JTextField(10);
        etiquetaNumeroCuenta = new JLabel("Número de cuenta:");
        campoNumeroCuenta = new JTextField(10);
        botonConsultar = new JButton("Consultar");
        etiquetaSaldo = new JLabel("Saldo:");
        campoSaldo = new JTextField(10);
        campoSaldo.setEditable(false);

        // Crear panel
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // Agregar componentes iniciales al panel (vacío por ahora)
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(botonConsultar);
        panel.add(etiquetaSaldo);
        panel.add(campoSaldo);

        // Agregar panel al frame
        add(panel);

        setVisible(true);
    }

    // Getters para obtener los valores de los campos
    public String getIdUsuario() {
        return campoId.getText();
    }

    public String getNumeroCuenta() {
        return campoNumeroCuenta.getText();
    }

    // Métodos para mostrar/ocultar campos según la opción de consulta
    public void mostrarCampoId() {
        panel.removeAll();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.add(etiquetaId);
        panel.add(campoId);
        panel.add(new JLabel());
        panel.add(botonConsultar);
        panel.add(etiquetaSaldo);
        panel.add(campoSaldo);
        panel.revalidate();
        panel.repaint();
    }

    public void mostrarCampoNumeroCuenta() {
        panel.removeAll();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.add(etiquetaNumeroCuenta);
        panel.add(campoNumeroCuenta);
        panel.add(new JLabel());
        panel.add(botonConsultar);
        panel.add(etiquetaSaldo);
        panel.add(campoSaldo);
        panel.revalidate();
        panel.repaint();
    }

    // Método para mostrar el saldo
    public void mostrarSaldo(double saldo) {
        campoSaldo.setText(String.valueOf(saldo));
    }

    // Método para mostrar un mensaje de error
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Método para agregar ActionListener al botón
    public void agregarActionListener(ActionListener listener) {
        botonConsultar.addActionListener(listener);
    }

    public JButton getBotonConsultar() {
        return botonConsultar;
    }
}