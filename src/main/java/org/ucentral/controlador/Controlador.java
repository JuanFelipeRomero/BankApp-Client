package org.ucentral.controlador;
import org.ucentral.comunicacionServidor.ComunicadorServidor;
import org.ucentral.dto.RespuestaDTO;
import org.ucentral.dto.Transaccion;
import org.ucentral.vista.*;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class Controlador implements ActionListener {

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
    private static String correoLogin;
    private static String loginIdSession;
    private static String numeroCuentaLogin;
    private static String nombreLogin;
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
            while (intentos < 10 && !conectado) {
                // Reintenta conectarse y envía ping
                comunicadorServidor.conectar();
                if (comunicadorServidor.enviarPing()) {
                    conectado = true;
                    SwingUtilities.invokeLater(() -> {
                        ventanaEspera.dispose();
                        JOptionPane.showMessageDialog(null, "Conexión establecida con el servidor!");
                        ventanaPrincipalN.setVisible(true);
                        ventanaPrincipalN.agregarActionListener(Controlador.this);
                    });
                    break;
                }
                intentos++;
                System.out.println("Intentos de reconexion: " + intentos);
                try {
                    Thread.sleep(3000);
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

        //Bton para login
        else if (ventanaInicioSesion != null && e.getSource() == ventanaInicioSesion.getBotonIniciarSesion() ) {
            if(manejarLogin()){
                if (ventanaInicio == null) {
                    ventanaInicio = new VentanaInicio();
                    ventanaInicio.agregarActionListener(this); // Agregar el listener a VentanaInicio
                    ventanaInicio.setVisible(true);
                    ventanaInicio.setNombreUsuario(nombreLogin);
                    ventanaInicio.setEtiquetaCuentaUsuario(numeroCuentaLogin);
                    ventanaInicio.setEtiquetaSaldoActual(saldoLogin);
                }

                ventanaInicio.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        manejarLogout(); // Llamar logout al cerrar ventana
                    }
                });
            }
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
            ventanaInicio.dispose();
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
                + "\"idSesion\": \"" +  loginIdSession + "\","
                + "\"correo\": \"" + correoLogin + "\""
                + "}";

        String solicitudLogout = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

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
                + "\"idSesion\": \"" + loginIdSession + "\","
                + "\"numeroCuentaDestino\": \"" + numeroCuentaDestino + "\","
                + "\"monto\": " + monto
                + "}";

        String solicitudConsignacion = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";


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
                + "\"idSesion\": \"" + loginIdSession + "\","
                + "\"numeroCuentaDestino\": \"" + numeroCuentaDestino + "\","
                + "\"monto\": " + monto
                + "}";

        String solicitudConsignacion = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";


        // Enviar solicitud de consignacion al sevidor
        String respuestaDeposito = comunicadorServidor.enviarSolicitud(solicitudConsignacion);

        // Procesar la respuesta del servidor
        procesarRespuestaDeposito(respuestaDeposito);
    }

    private void manejarVerMovimientos() {
        String tipoOperacion = "historial_transacciones";
        String datos =  "{" + "\"idSesion\": \"" + loginIdSession + "\""
                        + "}";

        String solicitudVerMovimientos = "{"
                + "\"tipoOperacion\": \"" + tipoOperacion + "\","
                + "\"datos\": " + datos
                + "}";

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

    //Procesa la respuesta JSON del servidor al consultar el saldo.
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

    private boolean procesarRespuestaLogin(String respuesta) {
        if (respuesta != null && !respuesta.isEmpty()) {
            try {
                Gson gson = new Gson();
                RespuestaDTO resp = gson.fromJson(respuesta, RespuestaDTO.class);

                if (resp.getCodigo() == 200) {
                    // Éxito en el login
                    Map<String, Object> datos = resp.getDatos();
                    if (datos.containsKey("idSesion")) {
                        loginIdSession = datos.get("idSesion").toString();
                    }

                    if (datos.containsKey("correo")) {
                        correoLogin = datos.get("correo").toString();
                    }

                    if (datos.containsKey("numeroCuenta")) {
                        numeroCuentaLogin = datos.get("numeroCuenta").toString();
                    }

                    if (datos.containsKey("nombre")) {
                        nombreLogin = datos.get("nombre").toString();
                    }

                    if (datos.containsKey("saldo")) {
                        saldoLogin = datos.get("saldo").toString();
                    }

                    // Mostrar mensaje de éxito al usuario
                    String msg = "Bienvenid@ " + nombreLogin;
                    ventanaInicioSesion.mostrarMensaje(msg);
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
                    correoLogin = null;
                    loginIdSession = null;
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

                    if (datosConsignacion.containsKey("monto") || datosConsignacion.containsKey("numeroCuentaDestino")) {
                        String numeroCuentaDestino = datosConsignacion.get("numeroCuentaDestino").toString();
                        String monto = datosConsignacion.get("monto").toString();
                        String msg = "Consignacion realizada exitosamente\n" + "Monto consignado: $" + monto + "\ncuenta destino:  " + numeroCuentaDestino;
                        ventanaConsignar.mostrarMensaje(msg);
                    }

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

                    if (datosDeposito.containsKey("monto") && datosDeposito.containsKey("saldoNuevo")) {
                        String saldoNuevo = datosDeposito.get("saldoNuevo").toString();
                        String monto = datosDeposito.get("monto").toString();
                        String msg = "Deposito realiazado exitosamente\n Monto depositado: $" + monto +"\n Nuevo Saldo: $" + saldoNuevo;
                        ventanaDepositar.mostrarMensaje(msg);
                        ventanaInicio.setEtiquetaSaldoActual(saldoNuevo);
                    }
                    ventanaDepositar.dispose();

                } else {
                    // Error en la consignacion
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
                    Map<String, Object> registroMovimientos = resp.getDatos();

                    StringBuilder msg = new StringBuilder("Movimientos:\n");
                    for (Object value : registroMovimientos.values()) {
                        // Convertir cada objeto manualmente a Transaccion
                        Transaccion transaccion = gson.fromJson(gson.toJson(value), Transaccion.class);

                        // Construir la línea de salida con el formato especificado
                        msg.append("Fecha y Hora: ").append(transaccion.getFecha_hora()).append("\n")
                                .append("Tipo: ").append(transaccion.getTipo_transaccion()).append("\n")
                                .append("Monto: $").append(transaccion.getMonto()).append("\n")
                                .append("Cedula remitente: ").append(transaccion.getIdentificacion_origen()).append("\n")
                                .append("Cuenta Origen: ").append(transaccion.getCuenta_origen()).append("\n")
                                .append("Cuenta Destino: ").append(transaccion.getCuenta_destino()).append("\n")
                                .append("-----------------------------------------\n"); // Línea separadora
                    }

                    // Mostrar en la interfaz
                    System.out.println(msg.toString()); // Cambia esto por ventanaMovimientos.mostrarMensaje(msg.toString());
                    ventanaMovimientos.updateTextPane(String.valueOf(msg));
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