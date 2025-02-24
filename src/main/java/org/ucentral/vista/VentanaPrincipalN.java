package org.ucentral.vista;
import org.ucentral.comunicacionServidor.ComunicadorServidor;

import javax.swing.*;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaPrincipalN  extends JFrame{
    private JPanel contentPane;
    private JLabel appBancariaLabel;
    private JButton botonIniciarSesion;
    private JButton botonRegistrarse;
    private JButton botonConsultarSaldo;


    public VentanaPrincipalN() {
        // Asegurar que la UI generada por IntelliJ se usa correctamente
        setContentPane(contentPane);
        setTitle("Aplicación Bancaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 325);
        setLocationRelativeTo(null);

        // Conectar al servidor al iniciar la aplicación
        ComunicadorServidor.getInstance().conectar();

        setVisible(true);
    }

    // Getters para los botones
    public JButton getBotonRegistrarse() {
        return botonRegistrarse;
    }

    public JButton getBotonIniciarSesion() {
        return botonIniciarSesion;
    }

    public JButton getBotonConsultarSaldo() {return botonConsultarSaldo;}

    //Metodo para mostrar un mensaje de error
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //Metodo para agregar ActionListener a los botones
    public void agregarActionListener(ActionListener listener) {
        botonRegistrarse.addActionListener(listener);
        botonIniciarSesion.addActionListener(listener);
        botonConsultarSaldo.addActionListener(listener);
    }
}
