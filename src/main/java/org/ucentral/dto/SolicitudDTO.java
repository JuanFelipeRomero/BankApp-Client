package org.ucentral.dto;

import java.util.Map;

public class SolicitudDTO {
    private String tipoOperacion;
    private Map<String, Object> datos;

    // Getters y setters
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Map<String, Object> getDatos() {
        return datos;
    }

    public void setDatos(Map<String, Object> datos) {
        this.datos = datos;
    }
}