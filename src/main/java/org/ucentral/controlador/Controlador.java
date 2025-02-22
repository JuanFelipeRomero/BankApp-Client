package org.ucentral.controlador;

import org.ucentral.comunicacionServidor.ComunicadorServidor;
import org.ucentral.dto.RespuestaDTO;
import org.ucentral.vista.VentanaConsulta;
import org.ucentral.vista.VentanaOpcionesConsulta;
import org.ucentral.vista.VentanaPrincipal;
import org.ucentral.vista.VentanaRegistro;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class Controlador implements ActionListener {

    private VentanaPrincipal ventanaPrincipal;
    private VentanaRegistro ventanaRegistro;
    private VentanaOpcionesConsulta ventanaOpcionesConsulta;
    private VentanaConsulta ventanaConsulta;
    private ComunicadorServidor comunicadorServidor;

    public Controlador(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.ventanaPrincipal.setVisible(false);
        this.comunicadorServidor = ComunicadorServidor.getInstance();

        // Intentar conectar al servidor hasta que se logre la conexión o el usuario decida salir.
        while(!comunicadorServidor.isServidorActivo()) {
            comunicadorServidor.conectar();
            if (!comunicadorServidor.isServidorActivo()) {
                int respuesta = JOptionPane.showConfirmDialog(
                        null,
                        "No se pudo conectar al servidor. ¿Desea intentar nuevamente?",
                        "Error de conexión",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE
                );
                if (respuesta != JOptionPane.YES_OPTION) {
                    System.exit(0);  // Finaliza la aplicación si el usuario decide no reconectar
                }
            }
        }

        // Conexión establecida: ahora se muestra la ventana principal.
        JOptionPane.showMessageDialog(null, "Conexion establecida con exito!");
        this.ventanaPrincipal.setVisible(true);
        this.ventanaPrincipal.agregarActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Botón para mostrar la ventana de registro de nuevos usuarios
        if (e.getSource() == ventanaPrincipal.getBotonRegistrarse()) {
            if (ventanaRegistro == null) {
                ventanaRegistro = new VentanaRegistro();
                ventanaRegistro.agregarActionListener(this);
            }
            ventanaRegistro.setVisible(true);
        }
        // Botón para registrar un nuevo usuario
        else if (ventanaRegistro != null && e.getSource() == ventanaRegistro.getBotonRegistrar()) {
            manejarRegistro();
        }
        // Botón para mostrar la ventana de opciones de consulta (ya soy usuario)
        else if (e.getSource() == ventanaPrincipal.getBotonYaSoyUsuario()) {
            if (ventanaOpcionesConsulta == null) {
                ventanaOpcionesConsulta = new VentanaOpcionesConsulta();
                ventanaOpcionesConsulta.agregarActionListener(this);
            }
            ventanaOpcionesConsulta.setVisible(true);
        }
        // Botón para consultar saldo por identificación
        else if (ventanaOpcionesConsulta != null && e.getSource() == ventanaOpcionesConsulta.getBotonConsultarConCedula()) {
            mostrarVentanaConsultaId();
        }
        // Botón para consultar saldo por número de cuenta
        else if (ventanaOpcionesConsulta != null && e.getSource() == ventanaOpcionesConsulta.getBotonConsultarConCuenta()) {
            mostrarVentanaConsultaCuenta();
        }
        // Botón para realizar la consulta de saldo
        else if (ventanaConsulta != null && e.getSource() == ventanaConsulta.getBotonConsultar()) {
            manejarConsultaSaldo();
        }
    }

    /**
     * Maneja el proceso de registro de un nuevo usuario. -------------------------------------------------------------
     */
    private void manejarRegistro() {
        // Obtener datos de la ventana de registro
        String nombre = ventanaRegistro.getNombre();
        String id = ventanaRegistro.getCedula();
        String correo = ventanaRegistro.getCorreo();
        String contrasena = ventanaRegistro.getContrasena();

        // Validar los datos del usuario
        if (nombre.isEmpty() || id.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            ventanaRegistro.mostrarError("Debe completar todos los campos.");
            return;
        }

        // Crear solicitud de registro en formato JSON
        String tipoOperacion = "crear_cuenta";
        String datos = "{"
                + "\"nombre\": \"" + nombre + "\","
                + "\"identificacion\": " + id + ","
                + "\"correo\": \"" + correo + "\","
                + "\"contrasena\": \"" + contrasena + "\""
                + "}";

        String solicitudRegistro = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        // Enviar solicitud de registro al servidor
        String respuestaRegistro = comunicadorServidor.enviarSolicitud(solicitudRegistro);

        // Procesar la respuesta del servidor
        procesarRespuestaRegistro(respuestaRegistro);
    }

    /**
     * Muestra la ventana de consulta configurada para solicitar el ID. ----------------------------------------------------
     */
    private void mostrarVentanaConsultaId() {
        if (ventanaConsulta == null) {
            ventanaConsulta = new VentanaConsulta();
            ventanaConsulta.agregarActionListener(this);
        }
        ventanaConsulta.mostrarCampoCedula();
        ventanaConsulta.setVisible(true);
    }

    /**
     * Muestra la ventana de consulta configurada para solicitar el número de cuenta.---------------------------------------------------
     */
    private void mostrarVentanaConsultaCuenta() {
        if (ventanaConsulta == null) {
            ventanaConsulta = new VentanaConsulta();
            ventanaConsulta.agregarActionListener(this);
        }
        ventanaConsulta.mostrarCampoNumeroCuenta();
        ventanaConsulta.setVisible(true);
    }

    /**
     * Maneja la consulta de saldo según el ID o número de cuenta ingresado.-------------------------------------------------------
     */
    private void manejarConsultaSaldo() {
        String id = ventanaConsulta.getIdUsuario();
        String numeroCuenta = ventanaConsulta.getNumeroCuenta();

        // Validar que se haya ingresado al menos un dato
        if (id.isEmpty() && numeroCuenta.isEmpty()) {
            ventanaConsulta.mostrarError("Debe ingresar un ID o un número de cuenta.");
            return;
        }

        // Crear solicitud de consulta en formato JSON
        String tipoOperacion = "consulta_saldo";
        String datos;
        if (!id.isEmpty()) {
            datos = "{\"identificacion\": " + id + "}";
        } else {
            datos = "{\"numeroCuenta\": \"" + numeroCuenta + "\"}";
        }

        String solicitudConsulta = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        // Enviar solicitud de consulta y obtener la respuesta
        String respuestaConsulta = comunicadorServidor.enviarSolicitud(solicitudConsulta);
        procesarRespuestaConsulta(respuestaConsulta);
    }

    /**
     * Procesa la respuesta JSON del servidor al consultar el saldo.
     * Se asume que el servidor responde con un objeto JSON que incluye:
     *   - codigo
     *   - mensaje
     *   - datos (objeto que contiene saldo)
     */
    private void procesarRespuestaConsulta(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();
                // Convertir el JSON en un objeto RespuestaDTO
                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                // Verificar el código de la respuesta
                if (resp.getCodigo() == 200) {
                    // Éxito, extraer el saldo del mapa "datos"
                    Map<String, Object> datos = resp.getDatos();
                    if (datos != null && datos.containsKey("saldo")) {
                        Object saldoObj = datos.get("saldo");
                        // Convertirlo a double (puede ser Double, Integer, etc.)
                        double saldo = ((Number) saldoObj).doubleValue();
                        ventanaConsulta.mostrarSaldo(saldo);
                    } else {
                        ventanaConsulta.mostrarError("No se encontró el campo 'saldo' en la respuesta.");
                    }
                } else {
                    // Si el código no es 200, mostrar el mensaje de error del servidor
                    ventanaConsulta.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                // Error al parsear la respuesta
                ventanaConsulta.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaConsulta.mostrarError("Error al obtener respuesta del servidor (respuesta nula o vacía).");
        }
    }

    /**
     * Procesa la respuesta JSON del servidor al registrar un nuevo usuario.
     * Se asume que el servidor responde con un objeto JSON que incluye:
     *   - codigo
     *   - mensaje
     *   - datos (posiblemente el número de cuenta creado)
     */
    private void procesarRespuestaRegistro(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();
                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 201) {
                    // Éxito en la creación de la cuenta
                    Map<String, Object> datos = resp.getDatos();
                    String numeroCuenta = "";
                    String titular = "";
                    if (datos.containsKey("numeroCuenta")) {
                        numeroCuenta = datos.get("numeroCuenta").toString();
                    }
                    if (datos.containsKey("titular")) {
                        titular = datos.get("titular").toString();
                    }
                    // Mostrar mensaje de éxito al usuario
                    String msg = "Cuenta creada exitosamente.\n"
                            + "Titular: " + titular + "\n"
                            + "Número de cuenta: " + numeroCuenta;
                    ventanaRegistro.mostrarMensaje(msg);
                } else {
                    // Error en la creación de la cuenta
                    ventanaRegistro.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                ventanaRegistro.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaRegistro.mostrarError("Error al recibir respuesta del servidor (respuesta nula o vacía).");
        }
    }
}
