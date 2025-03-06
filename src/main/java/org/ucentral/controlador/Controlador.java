package org.ucentral.controlador;
import com.google.gson.reflect.TypeToken;
import org.ucentral.comunicacionServidor.ComunicadorServidor;
import org.ucentral.configLoader.ConfigLoader;
import org.ucentral.dto.RespuestaDTO;
import org.ucentral.dto.Transaccion;
import org.ucentral.vista.*;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Type;
import java.util.Map;

public class Controlador implements ActionListener {

    //Politica de reintentos
    private final int cantidadIntentos = ConfigLoader.getCantidadIntentos();
    private final int intervaloIntentos = ConfigLoader.getIntervaloIntentos();

    //Datos para respaldar solicitudes
    private String solicitudRespaldo = "";

    //ventanas-------------------
    private VentanaPrincipalN ventanaPrincipalN;
    private VentanaRegistroN ventanaRegistroN;
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaOpcionesConsultaN ventanaOpcionesConsultaN;
    private VentanaConsultaN ventanaConsultaN;
    private VentanaInicio ventanaInicio;
    private VentanaConsignar ventanaConsignar;
    private VentanaDepositar ventanaDepositar;
    private VentanaMovimientos ventanaMovimientos;
    private ComunicadorServidor comunicadorServidor;
    private static String loginToken;
    private static String loginIdSession;
    private static String nombreLogin;
    private static String correoLogin;
    private static String identificacionLogin;
    private static String numeroCuentaLogin;
    private static String saldoLogin;


    public Controlador(VentanaPrincipalN ventanaPrincipalN) {

        this.ventanaPrincipalN = ventanaPrincipalN;
        this.ventanaPrincipalN.setVisible(false);
        this.comunicadorServidor = ComunicadorServidor.getInstance();

        if (verificarConexion()) {
            ventanaPrincipalN.setVisible(true);
            ventanaPrincipalN.agregarActionListener(Controlador.this);
        }

    }

    private boolean verificarConexion() {
        // Intentar el ping inicial
        if (comunicadorServidor.enviarPing()) {
            return true;
        }

        // Si el ping falla, iniciar automáticamente la reconexión
        final JDialog ventanaEspera = new JDialog((JFrame) null, "Conectando", true);
        JLabel label = new JLabel("Conectando al servidor...", JLabel.CENTER);
        ventanaEspera.getContentPane().add(label);
        ventanaEspera.setSize(300, 100);
        ventanaEspera.setLocationRelativeTo(null);

        // Agregar listener para cerrar el programa si se cierra la ventana de espera
        ventanaEspera.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        new Thread(() -> {
            int intentos = 0;
            boolean conectado = false;
            while (intentos < cantidadIntentos && !conectado) {
                // Reintenta conectarse y envía ping
                comunicadorServidor.conectar();
                if (comunicadorServidor.enviarPing()) {
                    conectado = true;
                    SwingUtilities.invokeLater(() -> {
                        ventanaEspera.dispose();

                        //Cerrar ventanas y restablecer variables
                        cerrarTodasLasVentanas();
                        correoLogin = null;
                        loginIdSession = null;
                        numeroCuentaLogin = null;
                        nombreLogin = null;
                        saldoLogin = null;

                        JOptionPane.showMessageDialog(null, "Conexión establecida con el servidor!");
                        ventanaPrincipalN.setVisible(true);
                        ventanaPrincipalN.agregarActionListener(Controlador.this);
                    });
                    break;
                }
                intentos++;
                System.out.println("Intentos de reconexion: " + intentos);
                try {
                    Thread.sleep(intervaloIntentos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!conectado) {
                SwingUtilities.invokeLater(() -> {
                    ventanaEspera.dispose();
                    JOptionPane.showMessageDialog(null,
                            "No se pudo establecer la conexión con el servidor, intente en otro momento",
                            "Error de conexión", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                });
            }
        }).start();

        ventanaEspera.setVisible(true);

        return true;
    }

    private void respaldarSolicitud(String solicitud) {
        solicitudRespaldo = solicitud;
        System.out.println("Solicitud guardada: " + solicitudRespaldo);
    }

    private void cerrarTodasLasVentanas() {
        // Cerrar cada ventana si está abierta
        if (ventanaRegistroN != null) {
            ventanaRegistroN.dispose();
            ventanaRegistroN = null;
        }

        if (ventanaInicioSesion != null) {
            ventanaInicioSesion.dispose();
            ventanaInicioSesion = null;
        }

        if (ventanaOpcionesConsultaN != null) {
            ventanaOpcionesConsultaN.dispose();
            ventanaOpcionesConsultaN = null;
        }

        if (ventanaConsultaN != null) {
            ventanaConsultaN.dispose();
            ventanaConsultaN = null;
        }

        if (ventanaInicio != null) {
            ventanaInicio.dispose();
            ventanaInicio = null;
        }

        if (ventanaConsignar != null) {
            ventanaConsignar.dispose();
            ventanaConsignar = null;
        }

        if (ventanaDepositar != null) {
            ventanaDepositar.dispose();
            ventanaDepositar = null;
        }

        if (ventanaMovimientos != null) {
            ventanaMovimientos.dispose();
            ventanaMovimientos = null;
        }
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
            ventanaRegistroN.reiniciarCampos();
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
            ventanaInicioSesion.reiniciarCampos();
            ventanaInicioSesion.setVisible(true);
        }


        //Bton para login
        else if (ventanaInicioSesion != null && e.getSource() == ventanaInicioSesion.getBotonIniciarSesion() ) {
            if(manejarLogin()){
                if (ventanaInicio == null) {
                    ventanaInicio = new VentanaInicio();
                    ventanaInicio.agregarActionListener(this); // Agregar el listener a VentanaInicio
                    ventanaInicio.setVisible(true);
                    ventanaInicio.setNombreUsuario(nombreLogin);
                }

                ventanaInicio.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        manejarLogout(); // Llamar logout al cerrar ventana
                    }
                });
            }
        }

        // Botón para mostrar la ventana de opciones de consulta
        else if (ventanaInicio != null && e.getSource() == ventanaInicio.getBotonConsultarSaldo()) {
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


        //Botón para mostrar ventana para realizar consignaciones
        else if (ventanaInicio != null && e.getSource() == ventanaInicio.getBotonConsignar()) {
            if (ventanaConsignar == null) {
                ventanaConsignar = new VentanaConsignar();
                ventanaConsignar.agregarActionListener(this);
            }
            ventanaConsignar.reiniciarCampos();
            ventanaConsignar.setVisible(true);
        }

        //Boton para confirmar consignaciones
        else if (ventanaConsignar!= null && e.getSource() == ventanaConsignar.getBotonConsignar()) {
            manejarConsignacion();
        }

        // Nuevo bloque: Botón para mostrar la ventana de depositar a mi cuenta
        else if (ventanaInicio != null && e.getSource() == ventanaInicio.getbotonDepositarAMicuenta()) {
            if (ventanaDepositar == null) {
                ventanaDepositar = new VentanaDepositar();
                ventanaDepositar.agregarActionListener(this);
            }
            ventanaDepositar.reiniciarCampos();
            ventanaDepositar.setVisible(true);
            ventanaDepositar.setVisible(true);
        }

        //Boton para confirmar deposito
        else if (ventanaDepositar!= null && e.getSource() == ventanaDepositar.getBotonConfirmarDeposito()) {
            manejarDeposito();
        }

        //Botón para mostrar ventana de movimientos realizados
        else if (ventanaInicio != null && e.getSource() == ventanaInicio.getBotonVerMovimientos()) {
            if (ventanaMovimientos == null) {
                ventanaMovimientos = new VentanaMovimientos();
            }
            ventanaMovimientos.setVisible(true);
            manejarVerMovimientos();
        }

        //Boton para cerrar sesion
        else if (ventanaInicio!= null && e.getSource() == ventanaInicio.getBotonCerrarSesion()) {
            manejarLogout();
        }
    }

    //Maneja el proceso de registro de un nuevo usuario. -------------------------------------------------------------
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

        //Guardar solicitud en caso de que no se pueda realizar
        respaldarSolicitud(solicitudRegistro);

        // Enviar solicitud de registro al servidor
        String respuestaRegistro = comunicadorServidor.enviarSolicitud(solicitudRegistro);

        // Procesar la respuesta del servidor
        procesarRespuestaRegistro(respuestaRegistro);
    }

    //Maneja el proceso de login de usuarios. -------------------------------------------------------------
    private boolean manejarLogin() {
        //Obtener credenciales de los campos
        String correo = ventanaInicioSesion.getCorreo();
        String contrasena = ventanaInicioSesion.getContrasena();

        //Validar que los campos no esten vacios
        if (correo.isEmpty() || contrasena.isEmpty()) {
            ventanaInicioSesion.mostrarError("Debes completar todos los campos");
        }

        String tipoOperacion = "login";
        String datos = "{"
                + "\"correo\": \"" + correo + "\","
                + "\"contrasena\": \"" + contrasena + "\""
                + "}";

        String solicitudLogin = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        //Guardar solicitud en caso de que no se pueda realizar
        respaldarSolicitud(solicitudLogin);

        // Enviar solicitud de registro al servidor
        String respuestaLogin = comunicadorServidor.enviarSolicitud(solicitudLogin);

        // Procesar la respuesta del servidor
        return procesarRespuestaLogin(respuestaLogin);

    }

    //Maneja el proceso de logout de usuarios. -------------------------------------------------------------
    private void manejarLogout () {
        //Obtener los datos necesarios para logout
        String tipoOperacion = "logout";
        String datos =  "{"
                + "\"correo\": \"" +  correoLogin + "\","
                + "\"token\": \"" + loginToken + "\""
                + "}";

        String solicitudLogout = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        //Guardar solicitud en caso de que no se pueda realizar
        respaldarSolicitud(solicitudLogout);

        // Enviar solicitud de registro al servidor
        String respuestaLogout = comunicadorServidor.enviarSolicitud(solicitudLogout);

        // Procesar la respuesta del servidor
        procesarRespuestaLogout(respuestaLogout);
    }

    //Maneja el proceso de consignacion. -------------------------------------------------------------
    private void manejarConsignacion() {

        String numeroCuentaDestino = ventanaConsignar.getCampoNumeroCuentaDestino();
        String montoString =  ventanaConsignar.getCampoValorConsignar();

        // Validar que se haya ingresado al menos un dato
        if (numeroCuentaDestino.isEmpty() || montoString.isEmpty()) {
            ventanaConsignar.mostrarError("Debe completar los datos requeridos");
            return;
        }

        int monto = Integer.parseInt(montoString);

        if (monto <= 0) {
            ventanaConsignar.mostrarError("El monto a Consignar debe ser mayor a 0");
            return;
        }

        String tipoOperacion = "consigna_cuenta";
        String datos = "{"
                + "\"token\": \"" + loginToken + "\","
                + "\"numeroCuentaDestino\": \"" + numeroCuentaDestino + "\","
                + "\"monto\": " + monto
                + "}";

        String solicitudConsignacion = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        //Guardar solicitud en caso de que no se pueda realizar
        respaldarSolicitud(solicitudConsignacion);

        // Enviar solicitud de consignacion al sevidor
        String respuestaConsignacion = comunicadorServidor.enviarSolicitud(solicitudConsignacion);

        // Procesar la respuesta del servidor
        procesarRespuestaConsignacion(respuestaConsignacion);
    }

    //Maneja el proceso de Deposito -------------------------------------------------------------
    private void manejarDeposito () {

        String numeroCuentaDestino = numeroCuentaLogin;
        String montoString =  ventanaDepositar.getCampoValorDepositar();

        // Validar que se haya ingresado al menos un dato
        if (montoString.isEmpty()) {
            ventanaDepositar.mostrarError("Debe ingresar el valor a depositar");
            return;
        }

        int monto = Integer.parseInt(montoString);

        if (monto <= 0) {
            ventanaDepositar.mostrarError("El monto a depositar debe ser mayor a 0");
            return;
        }

        String tipoOperacion = "consigna_cuenta";
        String datos = "{"
                + "\"token\": \"" + loginToken + "\","
                + "\"numeroCuentaDestino\": \"" + numeroCuentaDestino + "\","
                + "\"monto\": " + monto
                + "}";

        String solicitudDeposito = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        //Guardar solicitud en caso de que no se pueda realizar
        respaldarSolicitud(solicitudDeposito);

        // Enviar solicitud de consignacion al sevidor
        String respuestaDeposito = comunicadorServidor.enviarSolicitud(solicitudDeposito);

        // Procesar la respuesta del servidor
        procesarRespuestaDeposito(respuestaDeposito);
    }

    //Maneja el proceso de consulta de movimientos ----------------------------------------------------
    private void manejarVerMovimientos() {
        String tipoOperacion = "historial_transacciones";
        String datos =  "{" + "\"token\": \"" + loginToken + "\""
                + "}";

        String solicitudVerMovimientos = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        //Guardar solicitud en caso de que no se pueda realizar
        respaldarSolicitud(solicitudVerMovimientos);

        String respuestaVerMovimientos = comunicadorServidor.enviarSolicitud(solicitudVerMovimientos);

        procesarRespuestaVerMovimientos(respuestaVerMovimientos);

    }

    //Muestra la ventana de consulta configurada para solicitar el ID. ----------------------------------------------------
    private void mostrarVentanaConsultaId() {
        if (ventanaConsultaN == null) {
            ventanaConsultaN = new VentanaConsultaN();
            ventanaConsultaN.agregarActionListener(this);
        }
        ventanaConsultaN.reiniciarCampos();
        ventanaConsultaN.mostrarCampoCedula();
        ventanaConsultaN.setVisible(true);
    }

    //Muestra la ventana de consulta configurada para solicitar el número de cuenta.---------------------------------------------------
    private void mostrarVentanaConsultaCuenta() {
        if (ventanaConsultaN== null) {
            ventanaConsultaN = new VentanaConsultaN();
            ventanaConsultaN.agregarActionListener(this);
        }
        ventanaConsultaN.reiniciarCampos();
        ventanaConsultaN.mostrarCampoNumeroCuenta();
        ventanaConsultaN.setVisible(true);
    }

    //Maneja la consulta de saldo según el ID o número de cuenta ingresado.-------------------------------------------------------
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
            datos = "{"
                    + "\"token\": \"" + loginToken + "\","
                    + "\"identificacion\": " + id
                    + "}";
        } else {
            datos = "{"
                    + "\"token\": \"" + loginToken + "\","
                    + "\"numeroCuenta\": \"" + numeroCuenta + "\""
                    + "}";
        }

        String solicitudConsulta = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

        //Guardar solicitud en caso de que no se pueda realizar
        respaldarSolicitud(solicitudConsulta);

        // Enviar solicitud de consulta y obtener la respuesta
        String respuestaConsulta = comunicadorServidor.enviarSolicitud(solicitudConsulta);
        procesarRespuestaConsulta(respuestaConsulta);
    }

    //Procesa la respuesta JSON del servidor al consultar el saldo.
    private void procesarRespuestaConsulta(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();
                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 200) {
                    Map<String, Object> datos = resp.getDatos();

                    if (datos != null && datos.containsKey("saldo")) {
                        // Obtener el valor del saldo y manejarlo de manera segura
                        Object saldoObj = datos.get("saldo");
                        double saldoValue = 0.0;

                        // Manejar diferentes tipos de datos que podrían venir en el saldo
                        if (saldoObj instanceof Number) {
                            saldoValue = ((Number) saldoObj).doubleValue();
                        } else if (saldoObj instanceof String) {
                            try {
                                saldoValue = Double.parseDouble(saldoObj.toString().trim());
                            } catch (NumberFormatException e) {
                                ventanaConsultaN.mostrarError("El formato del saldo recibido no es válido: " + saldoObj);
                                return;
                            }
                        }

                        ventanaConsultaN.mostrarSaldo(saldoValue);
                    } else {
                        ventanaConsultaN.mostrarError("No se encontró el campo 'saldo' en la respuesta.");
                    }
                } else {
                    ventanaConsultaN.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
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

                    String numeroCuenta = datos.getOrDefault("numeroCuenta", "").toString();
                    String titular = datos.getOrDefault("titular", "").toString();

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

    private boolean procesarRespuestaLogin(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();
                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 200) {
                    // Éxito en el login
                    Map<String, Object> datos = resp.getDatos();

                    loginToken = datos.getOrDefault("token", "").toString();
                    loginIdSession = datos.getOrDefault("idSesion", "").toString();
                    nombreLogin = datos.getOrDefault("nombre", "").toString();
                    correoLogin = datos.getOrDefault("correo", "").toString();
                    identificacionLogin = datos.getOrDefault("identificacion", "").toString();
                    numeroCuentaLogin = datos.getOrDefault("numeroCuenta", "").toString();

                    // Convertir saldo a String, asegurando que sea tratado correctamente
                    Object saldoObj = datos.get("saldo");
                    saldoLogin = (saldoObj != null) ? saldoObj.toString() : "0.00";

                    ventanaInicioSesion.mostrarMensaje("Bienvenid@ " + nombreLogin);
                    ventanaInicioSesion.dispose();

                    return true;

                } else {
                    // Error en inicio de sesion
                    ventanaInicioSesion.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                ventanaInicioSesion.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaInicioSesion.mostrarError("Error al recibir respuesta del servidor (respuesta nula o vacía).");
            return false;
        }
        return false;
    }

    private void procesarRespuestaLogout(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();
                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 200) {
                    // Limpiar datos de sesión
                    loginToken = null;
                    loginIdSession = null;
                    nombreLogin = null;
                    correoLogin = null;
                    identificacionLogin = null;
                    numeroCuentaLogin = null;
                    saldoLogin = null;

                    // Asegurarse de que ventanaInicio se establece a null si existe
                    if (ventanaInicio != null) {
                        ventanaInicio.dispose();
                        ventanaInicio = null;
                    }

                    System.out.println("Sesion cerrada exitosamente.");

                } else {
                    ventanaInicioSesion.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                ventanaInicioSesion.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaInicioSesion.mostrarError("Error al recibir respuesta del servidor (respuesta nula o vacía).");
        }
    }

    private void procesarRespuestaConsignacion(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();

                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 200) {
                    // Éxito en la consignacion
                    Map<String, Object> datosConsignacion = resp.getDatos();

                    String numeroCuentaDestino =  datosConsignacion.getOrDefault("numeroCuentaDestino", "").toString();
                    String monto = datosConsignacion.getOrDefault("monto", "").toString();
                    String msg = "Consignacion realizada exitosamente\n" + "Monto consignado: $" + monto + "\ncuenta destino:  " + numeroCuentaDestino;
                    ventanaConsignar.mostrarMensaje(msg);
                    ventanaConsignar.dispose();

                } else {
                    // Error en la consignacion
                    ventanaConsignar.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                ventanaConsignar.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaConsignar.mostrarError("Error al recibir respuesta del servidor (respuesta nula o vacía).");
        }
    }

    private void procesarRespuestaDeposito(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();

                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 200) {
                    // Éxito en el deposito
                    Map<String, Object> datosDeposito = resp.getDatos();

                    String saldoNuevo = datosDeposito.getOrDefault("saldoNuevo","").toString();
                    String monto = datosDeposito.getOrDefault("monto","").toString();
                    String msg = "Deposito realiazado exitosamente\n Monto depositado: $" + monto +"\n Nuevo Saldo: $" + saldoNuevo;
                    ventanaDepositar.mostrarMensaje(msg);

                    ventanaDepositar.dispose();

                } else {
                    // Error en el deposito
                    ventanaDepositar.mostrarError("Error: " + resp.getMensaje());
                }
            } catch (Exception ex) {
                ventanaDepositar.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaDepositar.mostrarError("Error al recibir respuesta del servidor (respuesta nula o vacía).");
        }
    }

    private void procesarRespuestaVerMovimientos(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {

                Gson gson = new Gson();
                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 200) {
                    // Convertir "datos" en un mapa de transacciones correctamente tipado
                    Type tipoMapa = new TypeToken<Map<String, Transaccion>>() {}.getType();
                    Map<String, Transaccion> registroMovimientos = gson.fromJson(gson.toJson(resp.getDatos()), tipoMapa);

                    // Construcción del mensaje de movimientos
                    StringBuilder msg = new StringBuilder("Movimientos:\n");
                    for (Transaccion transaccion : registroMovimientos.values()) {
                        msg.append("Fecha y Hora: ").append(transaccion.getFecha_hora()).append("\n")
                                .append("Tipo: ").append(transaccion.getTipo_transaccion()).append("\n")
                                .append("Monto: $").append(transaccion.getMonto()).append("\n")
                                .append("Cédula Remitente: ").append(transaccion.getIdentificacion_origen()).append("\n")
                                .append("Cuenta Origen: ").append(transaccion.getCuenta_origen()).append("\n")
                                .append("Cuenta Destino: ").append(transaccion.getCuenta_destino()).append("\n")
                                .append("-----------------------------------------\n");
                    }

                    // Mostrar en la interfaz
                    ventanaMovimientos.updateTextPane(msg.toString());
                } else {
                    System.out.println("Error: " + resp.getMensaje()); // Cambia esto por ventanaMovimientos.mostrarError("Error: " + resp.getMensaje());
                }


            } catch (Exception ex) {
                ventanaMovimientos.mostrarError("Error al parsear la respuesta del servidor: " + ex.getMessage());
            }
        } else {
            ventanaMovimientos.mostrarError("Error al recibir respuesta del servidor (respuesta nula o vacía).");
        }
    }
}