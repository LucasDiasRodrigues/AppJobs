package com.teamappjobs.appjobs.modelo;

import java.io.Serializable;
import java.util.Date;


public class Mensagem implements Serializable {


    private String remetente;
    private String destinatario;
    private String msg;
    private Date dthrEnviada;
    private Date dthrRecebida;
    private Date dthrVisualizada;
    private String situacao;
    private int codMsg;


    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDthrEnviada() {
        return dthrEnviada;
    }

    public void setDthrEnviada(Date dthrEnviada) {
        this.dthrEnviada = dthrEnviada;
    }

    public Date getDthrRecebida() {
        return dthrRecebida;
    }

    public void setDthrRecebida(Date dthrRecebida) {
        this.dthrRecebida = dthrRecebida;
    }

    public Date getDthrVisualizada() {
        return dthrVisualizada;
    }

    public void setDthrVisualizada(Date dthrVisualizada) {
        this.dthrVisualizada = dthrVisualizada;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }


    public int getCodMsg() {
        return codMsg;
    }

    public void setCodMsg(int codMsg) {
        this.codMsg = codMsg;
    }
}
