package com.betscr.modulovendedor.modelo.VO;

public class Recarga {

    private int num_recarga;
    private String fec_recarga;
    private String id_cliente;
    private int mon_recarga;
    private double por_comision;
    private int mon_comision;
    private int id_vendedor;

    public Recarga() {
    }

    public int getNum_recarga() {
        return num_recarga;
    }

    public void setNum_recarga(int num_recarga) {
        this.num_recarga = num_recarga;
    }

    public String getFec_recarga() {
        return fec_recarga;
    }

    public void setFec_recarga(String fec_recarga) {
        this.fec_recarga = fec_recarga;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getMon_recarga() {
        return mon_recarga;
    }

    public void setMon_recarga(int mon_recarga) {
        this.mon_recarga = mon_recarga;
    }

    public double getPor_comision() {
        return por_comision;
    }

    public void setPor_comision(double por_comision) {
        this.por_comision = por_comision;
    }

    public int getMon_comision() {
        return mon_comision;
    }

    public void setMon_comision(int mon_comision) {
        this.mon_comision = mon_comision;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }
}
