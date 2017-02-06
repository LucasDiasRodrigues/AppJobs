package com.teamappjobs.appjobs.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lucas on 26/04/2016.
 */
public class Vitrine implements Serializable {

    private int codVitrine;
    private String nome;
    private String descricao;
    private String FaixaPreco;
    private String parametroPreco;
    private int unidades;
    private String foto;
    private String localizacao;
    private String situacao;
    private String emailAnunciante;
    private String nomeAnunciante;
    private String telefoneAnunciante;
    private Date dataCriacao = new Date();
    private int subCategoria;
    private List<String> links = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String curtidas;
    private String seguidores;

    //Teste
    private String DescCategoria;


    public void addLink(String link) {
        if (!link.equals("")) {
            links.add(link);
        }
    }

    public void removeLink(String link) {
        for (String item : links) {
            if (item.equals(link)) {
                links.remove(item);
            }
        }
    }

    public void clearLinks() {
        links.clear();
    }

    public void addTag(String tag) {
        if (!tag.equals("")) {
            tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        for (String item : tags) {
            if (item.equals(tag)) {
                tags.remove(item);
            }
        }
    }

    public void clearTags() {
        tags.clear();
    }


    public int getCodVitrine() {
        return codVitrine;
    }

    public void setCodVitrine(int codVitrine) {
        this.codVitrine = codVitrine;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFaixaPreco() {
        return FaixaPreco;
    }

    public void setFaixaPreco(String faixaPreco) {
        FaixaPreco = faixaPreco;
    }

    public String getParametroPreco() {
        return parametroPreco;
    }

    public void setParametroPreco(String parametroPreco) {
        this.parametroPreco = parametroPreco;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getEmailAnunciante() {
        return emailAnunciante;
    }

    public void setEmailAnunciante(String emailAnunciante) {
        this.emailAnunciante = emailAnunciante;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(int subCategoria) {
        this.subCategoria = subCategoria;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescCategoria() {
        return DescCategoria;
    }

    public void setDescCategoria(String descCategoria) {
        DescCategoria = descCategoria;
    }

    public String  getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(String curtidas) {
        this.curtidas = curtidas;
    }

    public String getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(String seguidores) {
        this.seguidores = seguidores;
    }

    public String getNomeAnunciante() {
        return nomeAnunciante;
    }

    public void setNomeAnunciante(String nomeAnunciante) {
        this.nomeAnunciante = nomeAnunciante;
    }

    public String getTelefoneAnunciante() {
        return telefoneAnunciante;
    }

    public void setTelefoneAnunciante(String telefoneAnunciante) {
        this.telefoneAnunciante = telefoneAnunciante;
    }


}
