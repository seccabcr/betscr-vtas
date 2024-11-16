package com.betscr.modulovendedor.modelo.VO;

public class MovCtaUsuVO extends UsuarioVO {

    private int conse_mov;
    private String fechaMov;
    private String docRefe;
    private String detMov;
    private int montoMov;

    public MovCtaUsuVO() {
    }

    public String getFechaMov() {
        return fechaMov;
    }

    public void setFechaMov(String fechaMov) {
        this.fechaMov = fechaMov;
    }

    public String getDocRefe() {
        return docRefe;
    }

    public void setDocRefe(String docRefe) {
        this.docRefe = docRefe;
    }

    public String getDetMov() {
        return detMov;
    }

    public void setDetMov(String detMov) {
        this.detMov = detMov;
    }

    public int getMontoMov() {
        return montoMov;
    }

    public void setMontoMov(int montoMov) {
        this.montoMov = montoMov;
    }

    public int getConse_mov() {
        return conse_mov;
    }

    public void setConse_mov(int conse_mov) {
        this.conse_mov = conse_mov;
    }
}
