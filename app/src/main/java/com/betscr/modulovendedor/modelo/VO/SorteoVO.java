package com.betscr.modulovendedor.modelo.VO;

public class SorteoVO {

    private int codSorteo;
    private String nomSorteo;
    private int facPremio;
    private double comVendedor;
    private boolean diaLun;
    private boolean diaMar;
    private boolean diaMie;
    private boolean diaJue;
    private boolean diaVie;
    private boolean diaSab;
    private boolean diaDom;
    private String horaInicio;
    private String horaSorteo;
    private String horaCierre;
    private int reventado;
    private int num_digitos;

    public SorteoVO() {
    }

    public int getCodSorteo() {
        return codSorteo;
    }

    public void setCodSorteo(int codSorteo) {
        this.codSorteo = codSorteo;
    }

    public String getNomSorteo() {
        return nomSorteo;
    }

    public void setNomSorteo(String nomSorteo) {
        this.nomSorteo = nomSorteo;
    }

    public int getFacPremio() {
        return facPremio;
    }

    public void setFacPremio(int facPremio) {
        this.facPremio = facPremio;
    }

    public double getComVendedor() {
        return comVendedor;
    }

    public void setComVendedor(double comVendedor) {
        this.comVendedor = comVendedor;
    }

    public boolean isDiaLun() {
        return diaLun;
    }

    public void setDiaLun(boolean diaLun) {
        this.diaLun = diaLun;
    }

    public boolean isDiaMar() {
        return diaMar;
    }

    public void setDiaMar(boolean diaMar) {
        this.diaMar = diaMar;
    }

    public boolean isDiaMie() {
        return diaMie;
    }

    public void setDiaMie(boolean diaMie) {
        this.diaMie = diaMie;
    }

    public boolean isDiaJue() {
        return diaJue;
    }

    public void setDiaJue(boolean diaJue) {
        this.diaJue = diaJue;
    }

    public boolean isDiaVie() {
        return diaVie;
    }

    public void setDiaVie(boolean diaVie) {
        this.diaVie = diaVie;
    }

    public boolean isDiaSab() {
        return diaSab;
    }

    public void setDiaSab(boolean diaSab) {
        this.diaSab = diaSab;
    }

    public boolean isDiaDom() {
        return diaDom;
    }

    public void setDiaDom(boolean diaDom) {
        this.diaDom = diaDom;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraSorteo() {
        return horaSorteo;
    }

    public void setHoraSorteo(String horaSorteo) {
        this.horaSorteo = horaSorteo;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public String toString() {
        return nomSorteo;
    }

    public int getReventado() {
        return reventado;
    }

    public void setReventado(int reventado) {
        this.reventado = reventado;
    }

    public int getNum_digitos() {
        return num_digitos;
    }

    public void setNum_digitos(int num_digitos) {
        this.num_digitos = num_digitos;
    }
}
