package com.teamappjobs.appjobs.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lucas on 04/05/2016.
 */
public class Portifolio implements Serializable {

    private String imagem;
    private String descricao;
    private int codVitrine;
    private Date dataCadastro = new Date();


    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCodVitrine() {
        return codVitrine;
    }

    public void setCodVitrine(int codVitrine) {
        this.codVitrine = codVitrine;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
