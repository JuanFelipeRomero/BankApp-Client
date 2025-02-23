package org.ucentral.vista;
import javax.swing.border.AbstractBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaPrincipalN  extends JFrame{
    private JButton botonIniciarSesion;
    private JButton botonConsultarSaldo;
    private JButton botonRegistrarse;
    private JPanel contentPane;

    public VentanaPrincipalN () {

        setTitle("Aplicación Bancaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 325);
        setLocationRelativeTo(null);

        //Crear
        botonRegistrarse = new JButton("Registrarse");
        botonIniciarSesion = new JButton("Iniciar Sesion");
        botonConsultarSaldo = new JButton("Consultar Saldo");


        // Crear panel y agregar botones
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout());
        contentPane.add(botonRegistrarse);
        contentPane.add(botonIniciarSesion);
        contentPane.add(botonConsultarSaldo);

        add(contentPane);
        setVisible(true);
    }

    // Getters para los botones
    public JButton getBotonRegistrarse() {
        return botonRegistrarse;
    }

    public JButton getBotonYaSoyUsuario() {
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
        botonConsultarSaldo.addActionListener(listener);
    }
}
