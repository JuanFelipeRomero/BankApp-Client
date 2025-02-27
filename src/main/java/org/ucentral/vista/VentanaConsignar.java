    package org.ucentral.vista;

    import javax.swing.*;
    import java.awt.event.ActionListener;

    public class VentanaConsignar extends JFrame {
        private JTextField campoValorConsignar;
        private JTextField campoNumeroCuentaDestino;
        private JButton botonConfirmarConsignacion;
        private JPanel contentPane;
        private JLabel etiquetaCuentaDestino;
        private JLabel etiquetaValorConsignar;

        public VentanaConsignar() {
            setContentPane(contentPane);
            setTitle("Registrar Cliente");
            setSize(350, 380);
            setLocationRelativeTo(null);
            setVisible(true);
        }

        public String getCampoNumeroCuentaDestino() {
            return campoNumeroCuentaDestino.getText();
        }

        public String getCampoValorConsignar() {
            return campoValorConsignar.getText();
        }

        public JButton getBotonConsignar() {
            return botonConfirmarConsignacion;
        }

        public void mostrarMensaje(String msg) {
            JOptionPane.showMessageDialog(null, msg);
        }

        public void mostrarError(String mensaje) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }

        public void agregarActionListener(ActionListener listener) {
            botonConfirmarConsignacion.addActionListener(listener);
        }

    }
