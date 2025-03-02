package org.ucentral.dto;

public class Transaccion {
    private String tipo_transaccion;
    private double monto;
    private String fecha_hora;
    private String cuenta_origen;
    private String cuenta_destino;
    private String identificacion_origen;

    // Getters y setters
    public String getTipo_transaccion() {
        return tipo_transaccion;
    }

    public double getMonto() {
        return monto;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public String getCuenta_origen() {
        return cuenta_origen;
    }

    public String getCuenta_destino() {
        return cuenta_destino;
    }

    public String getIdentificacion_origen() {
        return identificacion_origen;
    }


    @Override
    public String toString() {
        return "Transaccion{" +
                "tipo_transaccion='" + tipo_transaccion + '\'' +
                ", monto=" + monto +
                ", fecha_hora='" + fecha_hora + '\'' +
                ", cuenta_origen='" + cuenta_origen + '\'' +
                ", cuenta_destino='" + cuenta_destino + '\'' +
                ", identificacion_origen='" + identificacion_origen + '\'' +
                '}';
    }
}
