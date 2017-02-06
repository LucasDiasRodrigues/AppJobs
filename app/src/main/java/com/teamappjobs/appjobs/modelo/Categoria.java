package com.teamappjobs.appjobs.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 27/04/2016.
 */
public class Categoria implements Serializable {

    private int codCategoria;
    private String categoria;
    private List<SubCategoria> subCategorias;


    @Override
    public String toString() {
        return categoria;
    }

    public Categoria(){
        subCategorias = new ArrayList<>();
    }


    public int getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(int codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<SubCategoria> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<SubCategoria> subCategorias) {
        this.subCategorias = subCategorias;
    }
}
