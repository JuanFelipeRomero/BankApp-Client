package org.ucentral.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaMovimientos extends JFrame {
    private JPanel contentPane;
    private JTextPane movimientosPanelTexto;
    private JScrollPane scrollPane;

    public VentanaMovimientos () {

        if(contentPane == null) {
            contentPane = new JPanel();
        }

        setContentPane(contentPane);
        setTitle("Movimientos realizdos");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateTextPane(final String text) {
        scrollPane.setViewportView(movimientosPanelTexto);
        movimientosPanelTexto.setText(text);
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
