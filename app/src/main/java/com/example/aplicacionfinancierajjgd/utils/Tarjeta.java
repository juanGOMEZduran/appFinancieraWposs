package com.example.aplicacionfinancierajjgd.utils;



public class Tarjeta {

    private int idTarjeta;
    private int idUsuario;

    public int getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(int idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreTarjeta() {
        return nombreTarjeta;
    }

    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(String expiracion) {
        this.expiracion = expiracion;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    private String nombreTarjeta;
    private String pan;
    private String expiracion;
    private String cvv;
    private double saldo;
    private boolean principal;


    public Tarjeta(int idTarjeta, int idUsuario, String nombreTarjeta, String pan,
                   String expiracion, String cvv, double saldo, boolean principal) {
        this.idTarjeta = idTarjeta;
        this.idUsuario = idUsuario;
        this.nombreTarjeta = nombreTarjeta;
        this.pan = pan;
        this.expiracion = expiracion;
        this.cvv = cvv;
        this.saldo = saldo;
        this.principal = principal;
    }




}
