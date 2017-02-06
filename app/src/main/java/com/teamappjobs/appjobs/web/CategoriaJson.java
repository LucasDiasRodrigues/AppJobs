package com.teamappjobs.appjobs.web;

import android.util.Log;

import com.teamappjobs.appjobs.modelo.Categoria;
import com.teamappjobs.appjobs.modelo.SubCategoria;
import com.teamappjobs.appjobs.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 27/04/2016.
 */
public class CategoriaJson {

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public Categoria JsonToCategoria(String data, JSONArray subcategorias) {
        Categoria categoria = new Categoria();
        List<SubCategoria> listSubCat = new ArrayList<>();

        try {
            JSONObject jo = new JSONObject(data);

            categoria.setCodCategoria(jo.getInt("cod_cat"));
            categoria.setCategoria(jo.getString("categoria"));

            for (int i = 0; i < subcategorias.length(); i++) {
                SubCategoria subCategoria = new SubCategoria();
                JSONObject jo2 = subcategorias.getJSONObject(i);
                if(jo2.getInt("cod_cat") == categoria.getCodCategoria()){
                    subCategoria.setCodSubCat(jo2.getInt("cod_subcat"));
                    subCategoria.setSubCat(jo2.getString("subcategoria"));
                    listSubCat.add(subCategoria);
                }

                categoria.setSubCategorias(listSubCat);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return categoria;
    }

    public List JsonArrayToListaCategorias(String data) {
        List<Categoria> list = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(data);

            JSONArray jsonCategorias = new JSONArray(jo.getString("categorias"));
            JSONArray jsonSubCategorias = new JSONArray(jo.getString("subcategorias"));

            for (int i = 0; i < jsonCategorias.length(); i++) {
                Categoria categoria = JsonToCategoria(jsonCategorias.getJSONObject(i).toString(), jsonSubCategorias);
                list.add(categoria);
                Log.i("CategoriasJson","json");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }



}
