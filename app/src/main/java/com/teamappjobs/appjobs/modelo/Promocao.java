package com.teamappjobs.appjobs.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 05/05/2016.
 */
public class Promocao implements Serializable {

    private int codPromocao;
    private String descricao;
    private String nome;
    private Date dataInicio = new Date();
    private Date dataFim = new Date();
    private String regras;
    private String situacao;
    private Date dataHrCriacao = new Date();
    private int codVitrine;
    private  String fotoVitrine;
    private String foto;

    private Vitrine vitrine;

    public int getCodPromocao() {
        return codPromocao;
    }

    public void setCodPromocao(int codPromocao) {
        this.codPromocao = codPromocao;
    }

    public String getNome() {return nome; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getRegras() {
        return regras;
    }

    public void setRegras(String regras) {
        this.regras = regras;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataHrCriacao() {
        return dataHrCriacao;
    }

    public void setDataHrCriacao(Date dataHrCriacao) {
        this.dataHrCriacao = dataHrCriacao;
    }

    public int getCodVitrine() {
        return codVitrine;
    }

    public void setCodVitrine(int codVitrine) {
        this.codVitrine = codVitrine;
    }


    public String getFotoVitrine() {
        return fotoVitrine;
    }
    public void setFotoVitrine(String fotoVitrine) {
        this.fotoVitrine = fotoVitrine;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }


    public Vitrine getVitrine() {
        return vitrine;
    }
    public void setVitrine(Vitrine vitrine) {
        this.vitrine = vitrine;
    }



}
