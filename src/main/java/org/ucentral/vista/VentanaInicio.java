package org.ucentral.vista;

import javax.swing.*;
import java.awt.event.ActionListener;

public class VentanaInicio extends JFrame {
    private JButton botonConsignar;
    private JButton botonVerMovimientos;
    private JButton botonDepositarAMicuenta;
    private JPanel separador1;
    private JPanel separador2;
    private JLabel etiquetaCuentaUsuario;
    private JLabel etiquetaSaldoActual;
    private JPanel contentPane;
    private JLabel etiquetaNombreUsuario;

    public VentanaInicio() {
        if(contentPane == null) {
            contentPane = new JPanel();
        }
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

    public void setNombreUsuario(String nombreUsuario) {
        etiquetaNombreUsuario.setText("Bienvenido: " + nombreUsuario);
    }

    public void setEtiquetaCuentaUsuario(String etiquetaCuentaUsuario) {
        this.etiquetaCuentaUsuario.setText("#Cuenta: " + etiquetaCuentaUsuario);
    }

    public void setEtiquetaSaldoActual(String etiquetaSaldoActual) {
        this.etiquetaSaldoActual.setText(etiquetaSaldoActual);
    }


    //Metodo para agregar el ActionListener a sus botones
    public void agregarActionListener(ActionListener listener) {
        botonConsignar.addActionListener(listener);
        botonDepositarAMicuenta.addActionListener(listener);
        botonVerMovimientos.addActionListener(listener);
    }
}
