package com.betscr.modulovendedor.modelo.VO;

public class VentaTktVO {

    private String numero;
    private int monto;
    private int montoRev =0;

    public VentaTktVO() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public int getMontoRev() {
        return montoRev;
    }

    public void setMontoRev(int montoRev) {
        this.montoRev = montoRev;
    }
}
