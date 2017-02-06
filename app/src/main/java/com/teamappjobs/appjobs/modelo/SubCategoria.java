package com.teamappjobs.appjobs.modelo;

import java.io.Serializable;

/**
 * Created by Lucas on 27/04/2016.
 */
public class SubCategoria implements Serializable {

    private int codSubCat;
    private String subCat;


    @Override
    public String toString() {
        return subCat;
    }

    public int getCodSubCat() {
        return codSubCat;
    }

    public void setCodSubCat(int codSubCat) {
        this.codSubCat = codSubCat;
    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }
}
