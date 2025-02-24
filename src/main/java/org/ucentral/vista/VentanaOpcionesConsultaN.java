package org.ucentral.vista;

import javax.swing.*;
import java.awt.event.ActionListener;

public class VentanaOpcionesConsultaN extends JFrame {
    private JButton botonConsultarConCedula;
    private JButton botonConsultarConCuenta;
    private JLabel consultarSaldoLabel;
    private JPanel contentPane;

    public VentanaOpcionesConsultaN() {
        setContentPane(contentPane);
        setTitle("Consultar saldo");
        setSize(350, 325);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JButton getBotonConsultarConCedula() {
        return botonConsultarConCedula;
    }

    public JButton getBotonConsultarConCuenta() {
        return botonConsultarConCuenta;
    }

    public void agregarActionListener (ActionListener listener) {
        botonConsultarConCedula.addActionListener(listener);
        botonConsultarConCuenta.addActionListener(listener);
    }

}
