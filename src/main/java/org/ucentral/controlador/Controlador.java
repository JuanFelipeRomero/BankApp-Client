package org.ucentral.controlador;

import org.ucentral.comunicacionServidor.ComunicadorServidor;
import org.ucentral.modelo.Usuario;
import org.ucentral.vista.VentanaConsulta;
import org.ucentral.vista.VentanaOpcionesConsulta;
import org.ucentral.vista.VentanaPrincipal;
import org.ucentral.vista.VentanaRegistro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador implements ActionListener {

    private VentanaPrincipal ventanaPrincipal;
    private VentanaRegistro ventanaRegistro;
    private VentanaOpcionesConsulta ventanaOpcionesConsulta;
    private VentanaConsulta ventanaConsulta;
    private ComunicadorServidor comunicadorServidor;

    public Controlador(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.comunicadorServidor = ComunicadorServidor.getInstance();
        this.ventanaPrincipal.agregarActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ventanaPrincipal.getBotonRegistrarse()) {
            // Mostrar VentanaRegistro
            if (ventanaRegistro == null) {
                ventanaRegistro = new VentanaRegistro();
                ventanaRegistro.agregarActionListener(this);
            }
            ventanaRegistro.setVisible(true);
        } else if (e.getSource() == ventanaPrincipal.getBotonYaSoyUsuario()) {
            // Mostrar VentanaOpcionesConsulta
            if (ventanaOpcionesConsulta == null) {
                ventanaOpcionesConsulta = new VentanaOpcionesConsulta();
                ventanaOpcionesConsulta.agregarActionListener(this);
            } else {
                ventanaOpcionesConsulta.setVisible(true);
            }
        } else if (e.getSource() == ventanaOpcionesConsulta.getBotonConsultarConId()) {
            // Mostrar VentanaConsulta con campo ID
            if (ventanaConsulta == null) {
                ventanaConsulta = new VentanaConsulta();
                ventanaConsulta.agregarActionListener(this);
            }
            ventanaConsulta.mostrarCampoId(); // Mostrar solo el campo ID
            ventanaConsulta.setVisible(true);
        } else if (e.getSource() == ventanaOpcionesConsulta.getBotonConsultarConCuenta()) {
            // Mostrar VentanaConsulta con campo número de cuenta
            if (ventanaConsulta == null) {
                ventanaConsulta = new VentanaConsulta();
                ventanaConsulta.agregarActionListener(this);
            }
            ventanaConsulta.mostrarCampoNumeroCuenta(); // Mostrar solo el campo número de cuenta
            ventanaConsulta.setVisible(true);
        } else if (e.getSource() == ventanaConsulta.getBotonConsultar()) {
            // Obtener valor ingresado por el usuario
            String id = ventanaConsulta.getIdUsuario();
            String numeroCuenta = ventanaConsulta.getNumeroCuenta();

            // TODO: Validar el ID o número de cuenta ingresado

            // TODO: Enviar solicitud de consulta al servidor
            // comunicacionServidor.enviarSolicitud(solicitudConsulta);

            // TODO: Procesar respuesta del servidor y mostrar saldo o mensaje de error
        } else if (e.getSource() == ventanaRegistro.getBotonRegistrar()) {
            // Obtener datos de la ventana de registro
            String nombre = ventanaRegistro.getNombre();
            String id = ventanaRegistro.getId();
            String correo = ventanaRegistro.getCorreo();
            String contrasena = ventanaRegistro.getContrasena();

            // TODO: Validar los datos del usuario

            // TODO: Enviar solicitud de registro al servidor
            String solicitudRegistro = String.format("{" +
                    "\"tipoOperacion\": \"crear_cuenta\"," +
                    "\"datos\": {" +
                    "\"nombre\": \"%s\"," +
                    "\"identificacion\": %s," +
                    "\"correo\": \"%s\"," +
                    "\"contrasena\": \"%s\"" +
                    "}" +
                    "}", nombre, id, correo, contrasena);

            // TODO: Procesar respuesta del servidor y mostrar mensaje al usuario
        }
    }

    // TODO: Implementar métodos para procesar respuestas del servidor y actualizar la vista
}
