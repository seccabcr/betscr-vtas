package com.betscr.modulovendedor.modelo.VO;

public class LiqDiaResumenVO {

    private String fecha_venta;
    private int mon_venta;
    private int mon_comision;
    private int mon_premio;

    public LiqDiaResumenVO() {
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

    public String getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(String fecha_venta) {
        this.fecha_venta = fecha_venta;
    }
}
