package com.betscr.modulovendedor.modelo.VO;

public class SorteoUsuVO extends SorteoVO {

    private int cod_usuario;
    //private int cod_sorteo;
    private double por_comision_usu;
    private int fac_premio_usu;
    private int fac_premio2;
    private int estado;

    public SorteoUsuVO() {
    }

    public int getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(int cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public double getPor_comision_usu() {
        return por_comision_usu;
    }

    public void setPor_comision_usu(double por_comision_usu) {
        this.por_comision_usu = por_comision_usu;
    }

    public int getFac_premio_usu() {
        return fac_premio_usu;
    }

    public void setFac_premio_usu(int fac_premio_usu) {
        this.fac_premio_usu = fac_premio_usu;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getFac_premio2() {
        return fac_premio2;
    }

    public void setFac_premio2(int fac_premio2) {
        this.fac_premio2 = fac_premio2;
    }
}
