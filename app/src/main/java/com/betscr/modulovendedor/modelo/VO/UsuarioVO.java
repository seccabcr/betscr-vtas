package com.betscr.modulovendedor.modelo.VO;

public class UsuarioVO {
    private int codSuc;
    private String idUsu;
    private String nomUsu;
    private String nomCom;
    private int tipoUsu;
    private String direccion;
    private int limiteVenta;
    private double porComision;
    private String titTKT;
    private String msgTKT;
    private int estado;

    public UsuarioVO() {
    }

    public int getCodSuc() {
        return codSuc;
    }

    public void setCodSuc(int codSuc) {
        this.codSuc = codSuc;
    }

    public String getCodUsu() {
        return idUsu;
    }

    public void setCodUsu(String codUsu) {
        this.idUsu = codUsu;
    }

    public String getNomUsu() {
        return nomUsu;
    }

    public void setNomUsu(String nomUsu) {
        this.nomUsu = nomUsu;
    }

    public String getNomCom() {
        return nomCom;
    }

    public void setNomCom(String nomCom) {
        this.nomCom = nomCom;
    }

    public int getTipoUsu() {
        return tipoUsu;
    }

    public void setTipoUsu(int tipoUsu) {
        this.tipoUsu = tipoUsu;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getLimiteVenta() {
        return limiteVenta;
    }

    public void setLimiteVenta(int limiteVenta) {
        this.limiteVenta = limiteVenta;
    }

    public double getPorComision() {
        return porComision;
    }

    public void setPorComision(double porComision) {
        this.porComision = porComision;
    }

    public String getTitTKT() {
        return titTKT;
    }

    public void setTitTKT(String titTKT) {
        this.titTKT = titTKT;
    }

    public String getMsgTKT() {
        return msgTKT;
    }

    public void setMsgTKT(String msgTKT) {
        this.msgTKT = msgTKT;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
