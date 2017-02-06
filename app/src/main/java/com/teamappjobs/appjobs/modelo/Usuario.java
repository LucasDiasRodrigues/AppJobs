package com.teamappjobs.appjobs.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lucas on 23/04/2016.
 */
public class Usuario implements Serializable {

    private String nome;
    private String sobreNome;

    private String email;
    private String sexo;
    private String imagemPerfil;
    private Date dataNasc = new Date();
    private String senha;
    private String situacao;
    private Date dataCadastro = new Date();
    private String gcmIdAtual;
    private List<String> telefones = new ArrayList<>();
    private List<String> gcmIds = new ArrayList<>();


    public void addGcmId(String gcmId){
        if(!gcmId.equals("")){
            gcmIds.add(gcmId);
        }
    }

    public void addTelefone(String telefone){
        if(!telefone.equals("")){
            telefones.add(telefone);
        }
    }

    public void removeTelefone(String telefone){
        for (String item : telefones){
            if(item.equals(telefone)) {
                telefones.remove(item);
            }
        }
    }
    public void clearTelefones(){
        telefones.clear();
    }



    //Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefone(List<String> telefones) {
        this.telefones = telefones;
    }

    public List<String> getGcmIds() {
        return gcmIds;
    }

    public void setGcmIds(List<String> gcmIds) {
        this.gcmIds = gcmIds;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public String getGcmIdAtual() {
        return gcmIdAtual;
    }

    public void setGcmIdAtual(String gcmIdAtual) {
        this.gcmIdAtual = gcmIdAtual;
    }
}
