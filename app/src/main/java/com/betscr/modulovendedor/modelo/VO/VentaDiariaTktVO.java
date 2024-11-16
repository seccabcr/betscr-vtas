package com.betscr.modulovendedor.modelo.VO;

public class VentaDiariaTktVO {

    private int num_tkt;
    private int mon_tkt;
    private String nom_cliente;
    private int tipo_jugada;

    public VentaDiariaTktVO() {
    }

    public int getNum_tkt() {
        return num_tkt;
    }

    public void setNum_tkt(int num_tkt) {
        this.num_tkt = num_tkt;
    }

    public int getMon_tkt() {
        return mon_tkt;
    }

    public void setMon_tkt(int mon_tkt) {
        this.mon_tkt = mon_tkt;
    }

    public String getNom_cliente() {
        return nom_cliente;
    }

    public void setNom_cliente(String nom_cliente) {
        this.nom_cliente = nom_cliente;
    }

    public int getTipo_jugada() {
        return tipo_jugada;
    }

    public void setTipo_jugada(int tipo_jugada) {
        this.tipo_jugada = tipo_jugada;
    }
}
