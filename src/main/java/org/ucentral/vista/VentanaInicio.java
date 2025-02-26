package org.ucentral.vista;

import javax.swing.*;

public class VentanaInicio extends JFrame {
    private JButton botonConsignar;
    private JButton botonVerMovimientos;
    private JButton botonDepositarAMicuenta;
    private JPanel separador1;
    private JPanel separador2;
    private JLabel etiquetaCuentaUsuario;
    private JLabel etiquetaSaldoActual;
    private JPanel contentPane;

    public VentanaInicio() {
        setContentPane(contentPane);
        setTitle("Iniciar Sesion");
        setSize(350, 325);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JButton getBotonConsignar() {
        return botonConsignar;
    }

    public JButton getbotonDepositarAMicuenta() {
        return botonDepositarAMicuenta;
    }

    public JButton getBotonVerMovimientos() {
        return botonVerMovimientos;
    }

}
