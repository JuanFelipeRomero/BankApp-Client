package org.ucentral.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaConsultaN extends JFrame {
    private JTextField campoCedula;
    private JTextField campoNumeroCuenta;
    private JButton botonConsultar;
    private JTextField campoSaldo;
    private JLabel etiquetaCedula;
    private JLabel etiquetaNumeroCuenta;
    private JLabel etiquetaSaldo;
    private JPanel contentPane;

    public VentanaConsultaN() {
        setContentPane(contentPane);
        setTitle("Consulta de Saldo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 380);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public String getCedula() {
        return campoCedula.getText();
    }

    public String getNumeroCuenta() {
        return campoNumeroCuenta.getText();
    }

    public JButton getBotonConsultar() {
        return botonConsultar;
    }

    // Métodos para mostrar/ocultar campos según la opción de consulta
    public void mostrarCampoCedula() {
        contentPane.removeAll();
        contentPane.setLayout(new GridLayout(6, 1, 10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        contentPane.add(etiquetaCedula);
        contentPane.add(campoCedula);
        contentPane.add(new JLabel());
        contentPane.add(botonConsultar);
        contentPane.add(etiquetaSaldo);
        contentPane.add(campoSaldo);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void mostrarCampoNumeroCuenta() {
        contentPane.removeAll();
        contentPane.setLayout(new GridLayout(6, 1, 10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        contentPane.add(etiquetaNumeroCuenta);
        contentPane.add(campoNumeroCuenta);
        contentPane.add(new JLabel());
        contentPane.add(botonConsultar);
        contentPane.add(etiquetaSaldo);
        contentPane.add(campoSaldo);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void agregarActionListener(ActionListener listener) {
        getBotonConsultar().addActionListener(listener);
    }

    //Metodo para mostrar el saldo
    public void mostrarSaldo(double saldo) {
        campoSaldo.setText(String.valueOf(saldo));
    }

    //Metodo para mostrar un mensaje de error
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }


}
