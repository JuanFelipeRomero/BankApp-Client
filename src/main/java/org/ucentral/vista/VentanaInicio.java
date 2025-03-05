package org.ucentral.vista;

import javax.swing.*;
import java.awt.event.ActionListener;

public class VentanaInicio extends JFrame {
    private JButton botonConsignar;
    private JButton botonVerMovimientos;
    private JButton botonDepositarAMicuenta;
    private JPanel separador1;
    private JPanel contentPane;
    private JLabel etiquetaNombreUsuario;
    private JPanel separador3;
    private JButton botonCerrarSesion;
    private JButton botonConsultarSaldo;

    public VentanaInicio() {
        if(contentPane == null) {
            contentPane = new JPanel();
        }
        setContentPane(contentPane);
        setTitle("Iniciar Sesion");
        setSize(350, 340);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JButton getBotonConsultarSaldo () {
        return botonConsultarSaldo;
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

    public JButton getBotonCerrarSesion () {
        return botonCerrarSesion;
    }

    public void setNombreUsuario(String nombreUsuario) {
        etiquetaNombreUsuario.setText("Bienvenid@: " + nombreUsuario);
    }


    //Metodo para agregar el ActionListener a sus botones
    public void agregarActionListener(ActionListener listener) {
        botonConsignar.addActionListener(listener);
        botonDepositarAMicuenta.addActionListener(listener);
        botonVerMovimientos.addActionListener(listener);
        botonConsultarSaldo.addActionListener(listener);
        botonCerrarSesion.addActionListener(listener);
    }
}
