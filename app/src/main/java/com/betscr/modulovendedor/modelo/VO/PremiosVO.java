package com.betscr.modulovendedor.modelo.VO;

public class PremiosVO {

    private int num_tkt;
    private int mon_jugado;
    private int mon_jugado_rev;
    private int mon_premio;
    private int mon_premio_rev;
    private String referencia;

    public PremiosVO() {
    }

    public int getNum_tkt() {
        return num_tkt;
    }

    public void setNum_tkt(int num_tkt) {
        this.num_tkt = num_tkt;
    }

    public int getMon_jugado() {
        return mon_jugado;
    }

    public void setMon_jugado(int mon_jugado) {
        this.mon_jugado = mon_jugado;
    }

    public int getMon_premio() {
        return mon_premio;
    }

    public void setMon_premio(int mon_premio) {
        this.mon_premio = mon_premio;
    }

    public String getReferencia() {
        return referencia;
    }

    public int getMon_jugado_rev() {
        return mon_jugado_rev;
    }

    public void setMon_jugado_rev(int mon_jugado_rev) {
        this.mon_jugado_rev = mon_jugado_rev;
    }

    public int getMon_premio_rev() {
        return mon_premio_rev;
    }

    public void setMon_premio_rev(int mon_premio_rev) {
        this.mon_premio_rev = mon_premio_rev;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}


