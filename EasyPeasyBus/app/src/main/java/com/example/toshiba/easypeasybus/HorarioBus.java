package com.example.toshiba.easypeasybus;

public class HorarioBus {

    private String primero;
    private String ultimo;
    private int frecuencia;

    public HorarioBus() {

    }

    public HorarioBus(String primero, String ultimo, int frecuencia) {
        this.primero = primero;
        this.ultimo = ultimo;
        this.frecuencia = frecuencia;
    }

    public String getPrimero() {
        return primero;
    }

    public void setPrimero(String primero) {
        this.primero = primero;
    }

    public String getUltimo() {
        return ultimo;
    }

    public void setUltimo(String ultimo) {
        this.ultimo = ultimo;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }
}
