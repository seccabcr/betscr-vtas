package com.betscr.modulovendedor.modelo.VO;

public class SorteoSucVO {

    private int cod_suc;
    private int cod_sorteo;
    private double por_comision_suc;
    private int fac_premio_suc;
    private int fac_premio_suc2;
    private int estado;

    public SorteoSucVO() {
    }

    public int getCod_suc() {
        return cod_suc;
    }

    public void setCod_suc(int cod_suc) {
        this.cod_suc = cod_suc;
    }

    public int getCod_sorteo() {
        return cod_sorteo;
    }

    public void setCod_sorteo(int cod_sorteo) {
        this.cod_sorteo = cod_sorteo;
    }

    public double getPor_comision_suc() {
        return por_comision_suc;
    }

    public void setPor_comision_suc(double por_comision_suc) {
        this.por_comision_suc = por_comision_suc;
    }

    public int getFac_premio_suc() {
        return fac_premio_suc;
    }

    public void setFac_premio_suc(int fac_premio_suc) {
        this.fac_premio_suc = fac_premio_suc;
    }

    public int getFac_premio_suc2() {
        return fac_premio_suc2;
    }

    public void setFac_premio_suc2(int fac_premio_suc2) {
        this.fac_premio_suc2 = fac_premio_suc2;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
