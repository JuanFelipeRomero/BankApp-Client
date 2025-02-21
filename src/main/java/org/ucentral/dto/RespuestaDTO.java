package org.ucentral.dto;

import java.util.Map;

public class RespuestaDTO {
    private int codigo;
    private String mensaje;
    private Map<String, Object> datos;

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public Map<String, Object> getDatos() {
        return datos;
    }
    public void setDatos(Map<String, Object> datos) {
        this.datos = datos;
    }
}