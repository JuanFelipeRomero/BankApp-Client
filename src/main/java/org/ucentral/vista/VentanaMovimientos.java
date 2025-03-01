package org.ucentral.vista;

import javax.swing.*;

public class VentanaMovimientos extends JFrame {
    private JPanel contentPane;
    private JTextPane movimientosPanelTexto;

    public VentanaMovimientos () {
        if(contentPane == null) {
            contentPane = new JPanel();
        }
        setContentPane(contentPane);
        setTitle("Movimientos realizdos");
        setSize(380, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater(() -> {
            movimientosPanelTexto.setText(movimientosPanelTexto.getText() + text);
            // Auto-scroll to the bottom
            movimientosPanelTexto.setCaretPosition(movimientosPanelTexto.getDocument().getLength());
        });
    }

}
