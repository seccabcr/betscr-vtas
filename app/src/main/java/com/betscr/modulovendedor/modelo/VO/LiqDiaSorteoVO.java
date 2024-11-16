package com.betscr.modulovendedor.modelo.VO;

public class LiqDiaSorteoVO extends SorteoVO {

    private String numPremiado;
    private int mon_venta;
    private int mon_comision;
    private int mon_premio;

    public LiqDiaSorteoVO() {
    }

    public int getMon_venta() {
        return mon_venta;
    }

    public void setMon_venta(int mon_venta) {
        this.mon_venta = mon_venta;
    }

    public int getMon_comision() {
        return mon_comision;
    }

    public void setMon_comision(int mon_comision) {
        this.mon_comision = mon_comision;
    }

    public int getMon_premio() {
        return mon_premio;
    }

    public void setMon_premio(int mon_premio) {
        this.mon_premio = mon_premio;
    }

    public String getNumPremiado() {
        return numPremiado;
    }

    public void setNumPremiado(String numPremiado) {
        this.numPremiado = numPremiado;
    }
}
