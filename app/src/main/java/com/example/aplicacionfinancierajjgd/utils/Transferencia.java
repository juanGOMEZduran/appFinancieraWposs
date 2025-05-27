package com.example.aplicacionfinancierajjgd.utils;

public class Transferencia {
    private int idTransferencia;
    private String panOrigen;
    private String panDestino;
    private double monto;
    private String descripcion;
    private String fecha;
    private String tipoTransaccion;

    public Transferencia(int idTransferencia, String panOrigen, String panDestino,
                         double monto, String descripcion, String fecha) {
        this.idTransferencia = idTransferencia;
        this.panOrigen = panOrigen;
        this.panDestino = panDestino;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipoTransaccion = "Transferencia"; // Puedes personalizar esto
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getPanDestino() {
        return panDestino;
    }

    public void setPanDestino(String panDestino) {
        this.panDestino = panDestino;
    }

    public String getPanOrigen() {
        return panOrigen;
    }

    public void setPanOrigen(String panOrigen) {
        this.panOrigen = panOrigen;
    }

    public int getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(int idTransferencia) {
        this.idTransferencia = idTransferencia;
    }
}