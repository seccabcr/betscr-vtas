package com.betscr.modulovendedor.modelo.VO;

public class SucursalVO {

    private int cod_suc;
    private String nom_suc;

    public SucursalVO() {
    }

    public int getCod_suc() {
        return cod_suc;
    }

    public void setCod_suc(int cod_suc) {
        this.cod_suc = cod_suc;
    }

    public String getNom_suc() {
        return nom_suc;
    }

    public void setNom_suc(String nom_suc) {
        this.nom_suc = nom_suc;
    }

    @Override
    public String toString() {
        return nom_suc;
    }
}
