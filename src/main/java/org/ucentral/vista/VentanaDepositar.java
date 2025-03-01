package org.ucentral.vista;
import javax.swing.*;
import java.awt.event.ActionListener;

public class VentanaDepositar extends JFrame {
    private JTextField campoValorDepositar;
    private JButton botonConfirmarDeposito;
    private JLabel etiquetaValorDepositar;
    private JPanel contentPane;

    // Nuevo constructor para configurar la ventana de depositar
    public VentanaDepositar() {
        if(contentPane == null) {
            contentPane = new JPanel();
        }
        setContentPane(contentPane);
        // ...inicialización de componentes y layout...
        setTitle("Depositar a Mi Cuenta");
        setSize(350, 325);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Configuración adicional de la UI
    }

    // Getters (si se requieren para asignar ActionListeners, etc.)
    public JTextField getCampoValorDepositar() {
        return campoValorDepositar;
    }

    public JButton getBotonConfirmarDeposito() {
        return botonConfirmarDeposito;
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void agregarActionListener(ActionListener listener) {
        botonConfirmarDeposito.addActionListener(listener);
    }
}