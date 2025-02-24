package org.ucentral.controlador;

import org.ucentral.comunicacionServidor.ComunicadorServidor;
import org.ucentral.dto.RespuestaDTO;
import org.ucentral.vista.*;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class Controlador implements ActionListener {

    private VentanaPrincipalN ventanaPrincipalN;
    private VentanaRegistroN ventanaRegistroN;
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaOpcionesConsultaN ventanaOpcionesConsultaN;
    private VentanaConsultaN ventanaConsultaN;
    private ComunicadorServidor comunicadorServidor;

    public Controlador(VentanaPrincipalN ventanaPrincipalN) {

        this.ventanaPrincipalN = ventanaPrincipalN;

        //-------------------
        this.ventanaPrincipalN.setVisible(false);
        this.comunicadorServidor = ComunicadorServidor.getInstance();

        // Intentar conectar al servidor hasta que se logre la conexión o el usuario decida salir.
        while(!comunicadorServidor.isServidorActivo()) {
            comunicadorServidor.conectar();
            verificarConexion();
        }

        // Conexión establecida: ahora se muestra la ventana principal.
        JOptionPane.showMessageDialog(null, "Conexion establecida con exito!");
        this.ventanaPrincipalN.setVisible(true);
        this.ventanaPrincipalN.agregarActionListener(this);
    }

    private boolean verificarConexion() {
        // Verificamos con el ping
        if (comunicadorServidor.enviarPing()) {
            return true;
        }
        // Si falla, mostramos el diálogo para intentar reconectar
        int respuesta = JOptionPane.showConfirmDialog(
                null,
                "No se pudo conectar al servidor. ¿Desea intentar nuevamente?",
                "Error de conexión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
        );
        if (respuesta == JOptionPane.YES_OPTION) {
            // Intentamos reconectar (esto crea un nuevo socket, etc.)
            comunicadorServidor.conectar();
            // Verificamos nuevamente con el ping
            return comunicadorServidor.enviarPing();
        }
        System.exit(0);
        return false;


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //verifica la conexion antes de cualquier operacion
        if (!verificarConexion()) {
            return;
        }

        // Botón para mostrar la ventana de registro de nuevos usuarios
        if (e.getSource() == ventanaPrincipalN.getBotonRegistrarse()) {

            if (ventanaRegistroN == null) {
                ventanaRegistroN = new VentanaRegistroN();
                ventanaRegistroN.agregarActionListener(this);
            }
            ventanaRegistroN.setVisible(true);
        }
        // Botón para registrar un nuevo usuario
        else if (ventanaRegistroN != null && e.getSource() == ventanaRegistroN.getBotonRegistrar()) {
            manejarRegistro();
        }

        //Boton para mostar pantalla de inicio de sesion
        else if (e.getSource() == ventanaPrincipalN.getBotonIniciarSesion()) {
            if (ventanaInicioSesion == null) {
                ventanaInicioSesion = new VentanaInicioSesion();
                ventanaInicioSesion.agregarActionListener(this);
            }
            ventanaInicioSesion.setVisible(true);
        }


        // Botón para mostrar la ventana de opciones de consulta
        else if (e.getSource() == ventanaPrincipalN.getBotonConsultarSaldo()) {
            if (ventanaOpcionesConsultaN == null) {
                ventanaOpcionesConsultaN= new VentanaOpcionesConsultaN();
                ventanaOpcionesConsultaN.agregarActionListener(this);
            }
            ventanaOpcionesConsultaN.setVisible(true);
        }


        // Botón para consultar saldo por identificación
        else if (ventanaOpcionesConsultaN != null && e.getSource() == ventanaOpcionesConsultaN.getBotonConsultarConCedula()) {
            mostrarVentanaConsultaId();
        }
        // Botón para consultar saldo por número de cuenta
        else if (ventanaOpcionesConsultaN != null && e.getSource() == ventanaOpcionesConsultaN.getBotonConsultarConCuenta()) {
            mostrarVentanaConsultaCuenta();
        }
        // Botón para realizar la consulta de saldo
        else if (ventanaConsultaN != null && e.getSource() == ventanaConsultaN.getBotonConsultar()) {
            manejarConsultaSaldo();
        }
    }

    /**
     * Maneja el proceso de registro de un nuevo usuario. -------------------------------------------------------------
     */
    private void manejarRegistro() {
        // Obtener datos de la ventana de registro
        String nombre = ventanaRegistroN.getNombre();
        String id = ventanaRegistroN.getCedula();
        String correo = ventanaRegistroN.getCorreo();
        String contrasena = ventanaRegistroN.getContrasena();

        // Validar los datos del usuario
        if (nombre.isEmpty() || id.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            ventanaRegistroN.mostrarError("Debe completar todos los campos.");
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
        if (ventanaConsultaN == null) {
            ventanaConsultaN = new VentanaConsultaN();
            ventanaConsultaN.agregarActionListener(this);
        }
        ventanaConsultaN.mostrarCampoCedula();
        ventanaConsultaN.setVisible(true);
    }

    /**
     * Muestra la ventana de consulta configurada para solicitar el número de cuenta.---------------------------------------------------
     */
    private void mostrarVentanaConsultaCuenta() {
        if (ventanaConsultaN== null) {
            ventanaConsultaN = new VentanaConsultaN();
            ventanaConsultaN.agregarActionListener(this);
        }
        ventanaConsultaN.mostrarCampoNumeroCuenta();
        ventanaConsultaN.setVisible(true);
    }

    /**
     * Maneja la consulta de saldo según el ID o número de cuenta ingresado.-------------------------------------------------------
     */
    private void manejarConsultaSaldo() {
        String id = ventanaConsultaN.getCedula();
        String numeroCuenta = ventanaConsultaN.getNumeroCuenta();

        // Validar que se haya ingresado al menos un dato
        if (id.isEmpty() && numeroCuenta.isEmpty()) {
            ventanaConsultaN.mostrarError("Debe ingresar un ID o un número de cuenta.");
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
                        ventanaConsultaN.mostrarSaldo(saldo);
                    } else {
                        ventanaConsultaN.mostrarError("No se encontró el campo 'saldo' en la respuesta.");
                    }
                } else {
                    // Si el código no es 200, mostrar el mensaje de error del servidor
                    ventanaConsultaN.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                // Error al parsear la respuesta
                ventanaConsultaN.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaConsultaN.mostrarError("Error al obtener respuesta del servidor (respuesta nula o vacía).");
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
                    ventanaRegistroN.mostrarMensaje(msg);
                    ventanaRegistroN.dispose();
                } else {
                    // Error en la creación de la cuenta
                    ventanaRegistroN.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                ventanaRegistroN.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaRegistroN.mostrarError("Error al recibir respuesta del servidor (respuesta nula o vacía).");
        }
    }
}
