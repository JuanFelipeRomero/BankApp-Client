package org.ucentral.modelo;

public class Usuario {
    private String id;
    private String nombre;
    private String correo;
    private CuentaBancaria cuentaBancaria;

    public Usuario(String id, String nombre, String correo, CuentaBancaria cuentaBancaria) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }
}
